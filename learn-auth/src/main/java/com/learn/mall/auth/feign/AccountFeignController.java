package com.learn.mall.auth.feign;

import cn.hutool.core.util.StrUtil;
import com.learn.mall.api.auth.bo.UserInfoInTokenBO;
import com.learn.mall.api.auth.dto.AuthAccountDTO;
import com.learn.mall.api.auth.feign.AccountFeignClient;
import com.learn.mall.api.auth.vo.AuthAccountVO;
import com.learn.mall.api.auth.vo.TokenInfoVO;
import com.learn.mall.auth.manager.TokenStore;
import com.learn.mall.auth.mapper.AuthAccountMapper;
import com.learn.mall.auth.model.AuthAccount;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.util.BeanUtil;
import com.learn.mall.common.util.PrincipalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountFeignController implements AccountFeignClient {

    @Autowired
    private AuthAccountMapper authAccountMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenStore tokenStore;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Long> save(AuthAccountDTO authAccountDTO) {
        if (!PrincipalUtil.isUserName(authAccountDTO.getUsername())) {
            return ServerResponseEntity.showFailMsg("用户名格式不正确");
        }
        AuthAccountVO exist = authAccountMapper.getByUsernameAndSysType(
                authAccountDTO.getUsername(), authAccountDTO.getSysType());
        if (exist != null) {
            return ServerResponseEntity.showFailMsg("用户名已存在");
        }

        Long newUid = authAccountMapper.getMaxUid() + 1;
        AuthAccount authAccount = BeanUtil.map(authAccountDTO, AuthAccount.class);
        authAccount.setUid(newUid);
        authAccount.setUserId(authAccountDTO.getUserId() != null ? authAccountDTO.getUserId() : newUid);
        authAccount.setTenantId(authAccountDTO.getTenantId() == null ? 0L : authAccountDTO.getTenantId());
        authAccount.setIsAdmin(authAccountDTO.getIsAdmin() == null ? 0 : authAccountDTO.getIsAdmin());
        authAccount.setStatus(authAccountDTO.getStatus() == null ? 1 : authAccountDTO.getStatus());
        authAccount.setCreateIp(StrUtil.blankToDefault(authAccountDTO.getCreateIp(), "127.0.0.1"));
        if (StrUtil.isNotBlank(authAccount.getPassword())) {
            authAccount.setPassword(passwordEncoder.encode(authAccount.getPassword()));
        }
        authAccountMapper.save(authAccount);
        return ServerResponseEntity.success(newUid);
    }

    @Override
    public ServerResponseEntity<TokenInfoVO> storeTokenAndGetVo(UserInfoInTokenBO userInfoInTokenBO) {
        return ServerResponseEntity.success(tokenStore.storeAndGetVo(userInfoInTokenBO));
    }

    @Override
    public ServerResponseEntity<AuthAccountVO> getByUsernameAndSysType(String username, Integer sysType) {
        return ServerResponseEntity.success(authAccountMapper.getByUsernameAndSysType(username, sysType));
    }
}
