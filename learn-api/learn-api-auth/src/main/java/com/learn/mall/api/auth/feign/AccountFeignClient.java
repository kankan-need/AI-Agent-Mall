package com.learn.mall.api.auth.feign;

import com.learn.mall.api.auth.bo.UserInfoInTokenBO;
import com.learn.mall.api.auth.constant.SysTypeEnum;
import com.learn.mall.api.auth.dto.AuthAccountDTO;
import com.learn.mall.api.auth.vo.AuthAccountVO;
import com.learn.mall.api.auth.vo.TokenInfoVO;
import com.learn.mall.common.feign.FeignInsideAuthConfig;
import com.learn.mall.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "learn-auth", contextId = "account")
public interface AccountFeignClient {

    @PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/account")
    ServerResponseEntity<Long> save(@RequestBody AuthAccountDTO authAccountDTO);

    @PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/storeTokenAndGetVo")
    ServerResponseEntity<TokenInfoVO> storeTokenAndGetVo(@RequestBody UserInfoInTokenBO userInfoInTokenBO);

    @PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getByUsernameAndSysType")
    ServerResponseEntity<AuthAccountVO> getByUsernameAndSysType(@RequestParam("userName") String username,
                                                                @RequestParam("sysType") Integer sysType);
}
