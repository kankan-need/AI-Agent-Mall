package com.learn.mall.rbac.feign;

import com.learn.mall.api.rbac.bo.UriPermissionBO;
import com.learn.mall.api.rbac.dto.ClearUserPermissionsCacheDTO;
import com.learn.mall.api.rbac.feign.PermissionFeignClient;
import com.learn.mall.common.response.ResponseEnum;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.rbac.service.MenuPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class PermissionFeignController implements PermissionFeignClient {

    @Autowired
    private MenuPermissionService menuPermissionService;

    @Override
    public ServerResponseEntity<Boolean> checkPermission(Long userId, Integer sysType, String uri,
                                                         Integer isAdmin, Integer method) {
        List<String> userPermissions = menuPermissionService.listUserPermissions(userId, sysType, Objects.equals(isAdmin, 1));
        List<UriPermissionBO> uriPermissions = menuPermissionService.listUriPermissionInfo(sysType);
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (UriPermissionBO uriPermission : uriPermissions) {
            if (pathMatcher.match(uriPermission.getUri(), uri) && Objects.equals(uriPermission.getMethod(), method)) {
                if (userPermissions.contains(uriPermission.getPermission())) {
                    return ServerResponseEntity.success(Boolean.TRUE);
                }
                return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
            }
        }
        return ServerResponseEntity.success(Boolean.TRUE);
    }

    @Override
    public ServerResponseEntity<Void> clearUserPermissionsCache(ClearUserPermissionsCacheDTO dto) {
        menuPermissionService.clearUserPermissionsCache(dto.getUserId(), dto.getSysType());
        return ServerResponseEntity.success();
    }
}
