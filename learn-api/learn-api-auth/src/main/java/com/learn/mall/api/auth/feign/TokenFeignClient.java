package com.learn.mall.api.auth.feign;

import com.learn.mall.api.auth.bo.UserInfoInTokenBO;
import com.learn.mall.common.constant.Auth;
import com.learn.mall.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "learn-auth", contextId = "token")
public interface TokenFeignClient {

    @GetMapping(Auth.CHECK_TOKEN_URI)
    ServerResponseEntity<UserInfoInTokenBO> checkToken(@RequestParam("accessToken") String accessToken);
}
