package com.learn.mall.auth.feign;

import com.learn.mall.api.auth.bo.UserInfoInTokenBO;
import com.learn.mall.api.auth.feign.TokenFeignClient;
import com.learn.mall.auth.manager.TokenStore;
import com.learn.mall.common.response.ServerResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenFeignController implements TokenFeignClient {

    @Autowired
    private TokenStore tokenStore;

    @Override
    public ServerResponseEntity<UserInfoInTokenBO> checkToken(String accessToken) {
        ServerResponseEntity<UserInfoInTokenBO> response = tokenStore.getUserInfoByAccessToken(accessToken, true);
        if (!response.isSuccess()) {
            return ServerResponseEntity.transform(response);
        }
        return ServerResponseEntity.success(response.getData());
    }
}
