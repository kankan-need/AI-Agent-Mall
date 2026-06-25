package com.learn.mall.auth.manager;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.learn.mall.api.auth.bo.UserInfoInTokenBO;
import com.learn.mall.api.auth.constant.SysTypeEnum;
import com.learn.mall.api.auth.vo.TokenInfoVO;
import com.learn.mall.common.cache.constant.CacheNames;
import com.learn.mall.common.response.ResponseEnum;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.security.bo.TokenInfoBO;
import com.learn.mall.common.util.PrincipalUtil;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class TokenStore {

    private final RedisTemplate<Object, Object> redisTemplate;
    private final RedisSerializer<Object> redisSerializer;
    private final StringRedisTemplate stringRedisTemplate;

    public TokenStore(RedisTemplate<Object, Object> redisTemplate,
                      RedisSerializer<Object> redisSerializer,
                      StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisSerializer = redisSerializer;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public TokenInfoBO storeAccessToken(UserInfoInTokenBO userInfoInToken) {
        TokenInfoBO tokenInfoBO = new TokenInfoBO();
        String accessToken = IdUtil.simpleUUID();
        String refreshToken = IdUtil.simpleUUID();
        tokenInfoBO.setUserInfoInToken(userInfoInToken);
        tokenInfoBO.setExpiresIn(getExpiresIn(userInfoInToken.getSysType()));

        String uidToAccessKeyStr = getUidToAccessKey(getApprovalKey(userInfoInToken));
        String accessKeyStr = getAccessKey(accessToken);
        String refreshToAccessKeyStr = getRefreshToAccessKey(refreshToken);

        List<String> existsAccessTokens = new ArrayList<>();
        existsAccessTokens.add(accessToken + StrUtil.COLON + refreshToken);

        Long size = redisTemplate.opsForSet().size(uidToAccessKeyStr);
        if (size != null && size != 0) {
            List<String> tokenInfoBoList = stringRedisTemplate.opsForSet().pop(uidToAccessKeyStr, size);
            if (tokenInfoBoList != null) {
                for (String accessTokenWithRefreshToken : tokenInfoBoList) {
                    String accessTokenData = accessTokenWithRefreshToken.split(StrUtil.COLON)[0];
                    if (BooleanUtil.isTrue(stringRedisTemplate.hasKey(getAccessKey(accessTokenData)))) {
                        existsAccessTokens.add(accessTokenWithRefreshToken);
                    }
                }
            }
        }

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            long expiresIn = tokenInfoBO.getExpiresIn();
            byte[] uidKey = uidToAccessKeyStr.getBytes(StandardCharsets.UTF_8);
            byte[] refreshKey = refreshToAccessKeyStr.getBytes(StandardCharsets.UTF_8);
            byte[] accessKey = accessKeyStr.getBytes(StandardCharsets.UTF_8);
            for (String existsAccessToken : existsAccessTokens) {
                connection.sAdd(uidKey, existsAccessToken.getBytes(StandardCharsets.UTF_8));
            }
            connection.expire(uidKey, expiresIn);
            connection.setEx(refreshKey, expiresIn, accessToken.getBytes(StandardCharsets.UTF_8));
            connection.setEx(accessKey, expiresIn, Objects.requireNonNull(redisSerializer.serialize(userInfoInToken)));
            return null;
        });

        tokenInfoBO.setAccessToken(encryptToken(accessToken, userInfoInToken.getSysType()));
        tokenInfoBO.setRefreshToken(encryptToken(refreshToken, userInfoInToken.getSysType()));
        return tokenInfoBO;
    }

    public ServerResponseEntity<UserInfoInTokenBO> getUserInfoByAccessToken(String accessToken, boolean needDecrypt) {
        if (StrUtil.isBlank(accessToken)) {
            return ServerResponseEntity.showFailMsg("accessToken is blank");
        }
        String realAccessToken;
        if (needDecrypt) {
            ServerResponseEntity<String> decryptTokenEntity = decryptToken(accessToken);
            if (!decryptTokenEntity.isSuccess()) {
                return ServerResponseEntity.transform(decryptTokenEntity);
            }
            realAccessToken = decryptTokenEntity.getData();
        } else {
            realAccessToken = accessToken;
        }
        UserInfoInTokenBO userInfoInTokenBO = (UserInfoInTokenBO) redisTemplate.opsForValue().get(getAccessKey(realAccessToken));
        if (userInfoInTokenBO == null) {
            return ServerResponseEntity.showFailMsg("accessToken 已过期");
        }
        return ServerResponseEntity.success(userInfoInTokenBO);
    }

    public void deleteAllToken(String appId, Long uid) {
        String uidKey = getUidToAccessKey(getApprovalKey(appId, uid));
        Long size = redisTemplate.opsForSet().size(uidKey);
        if (size == null || size == 0) {
            return;
        }
        List<String> tokenInfoBoList = stringRedisTemplate.opsForSet().pop(uidKey, size);
        if (CollUtil.isEmpty(tokenInfoBoList)) {
            return;
        }
        for (String accessTokenWithRefreshToken : tokenInfoBoList) {
            String[] parts = accessTokenWithRefreshToken.split(StrUtil.COLON);
            redisTemplate.delete(getRefreshToAccessKey(parts[1]));
            redisTemplate.delete(getAccessKey(parts[0]));
        }
        redisTemplate.delete(uidKey);
    }

    public TokenInfoVO storeAndGetVo(UserInfoInTokenBO userInfoInToken) {
        TokenInfoBO tokenInfoBO = storeAccessToken(userInfoInToken);
        TokenInfoVO tokenInfoVO = new TokenInfoVO();
        tokenInfoVO.setAccessToken(tokenInfoBO.getAccessToken());
        tokenInfoVO.setRefreshToken(tokenInfoBO.getRefreshToken());
        tokenInfoVO.setExpiresIn(tokenInfoBO.getExpiresIn());
        return tokenInfoVO;
    }

    private int getExpiresIn(int sysType) {
        int expiresIn = 3600;
        if (Objects.equals(sysType, SysTypeEnum.ORDINARY.value())
                || Objects.equals(sysType, SysTypeEnum.MULTISHOP.value())
                || Objects.equals(sysType, SysTypeEnum.PLATFORM.value())) {
            expiresIn = expiresIn * 24 * 30;
        }
        return expiresIn;
    }

    private String encryptToken(String accessToken, Integer sysType) {
        return Base64.encode(accessToken + System.currentTimeMillis() + sysType);
    }

    private ServerResponseEntity<String> decryptToken(String data) {
        try {
            String decryptStr = Base64.decodeStr(data);
            String decryptToken = decryptStr.substring(0, 32);
            long createTokenTime = Long.parseLong(decryptStr.substring(32, 45));
            int sysType = Integer.parseInt(decryptStr.substring(45));
            if (System.currentTimeMillis() - createTokenTime > getExpiresIn(sysType) * 1000L) {
                return ServerResponseEntity.showFailMsg("token 格式有误");
            }
            if (!PrincipalUtil.isSimpleChar(decryptToken)) {
                return ServerResponseEntity.showFailMsg("token 格式有误");
            }
            return ServerResponseEntity.success(decryptToken);
        } catch (Exception e) {
            return ServerResponseEntity.showFailMsg("token 格式有误");
        }
    }

    private static String getApprovalKey(UserInfoInTokenBO userInfoInToken) {
        return getApprovalKey(userInfoInToken.getSysType().toString(), userInfoInToken.getUid());
    }

    private static String getApprovalKey(String appId, Long uid) {
        return uid == null ? appId : appId + StrUtil.COLON + uid;
    }

    private String getAccessKey(String accessToken) {
        return CacheNames.ACCESS + accessToken;
    }

    private String getUidToAccessKey(String approvalKey) {
        return CacheNames.UID_TO_ACCESS + approvalKey;
    }

    private String getRefreshToAccessKey(String refreshToken) {
        return CacheNames.REFRESH_TO_ACCESS + refreshToken;
    }
}
