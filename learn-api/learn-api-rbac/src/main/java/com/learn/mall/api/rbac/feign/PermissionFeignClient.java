package com.learn.mall.api.rbac.feign;

import com.learn.mall.api.rbac.dto.ClearUserPermissionsCacheDTO;
import com.learn.mall.common.feign.FeignInsideAuthConfig;
import com.learn.mall.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "learn-rbac", contextId = "permission")
public interface PermissionFeignClient {

    @GetMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/permission/checkPermission")
    ServerResponseEntity<Boolean> checkPermission(@RequestParam("userId") Long userId,
                                                  @RequestParam("sysType") Integer sysType,
                                                  @RequestParam("uri") String uri,
                                                  @RequestParam("isAdmin") Integer isAdmin,
                                                  @RequestParam("method") Integer method);

    @PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/permission/clearUserPermissionsCache")
    ServerResponseEntity<Void> clearUserPermissionsCache(@RequestBody ClearUserPermissionsCacheDTO dto);
}
