package com.learn.mall.api.user.feign;

import com.learn.mall.api.user.vo.UserAddrFeignVO;
import com.learn.mall.common.feign.FeignInsideAuthConfig;
import com.learn.mall.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "learn-user", contextId = "userAddr")
public interface UserAddrFeignClient {

    @GetMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/user_addr")
    ServerResponseEntity<UserAddrFeignVO> getAddr(@RequestParam("addrId") Long addrId,
                                                  @RequestParam("userId") Long userId);
}
