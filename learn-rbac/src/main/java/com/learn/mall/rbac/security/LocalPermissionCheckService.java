package com.learn.mall.rbac.security;

import com.learn.mall.api.rbac.bo.UriPermissionBO;
import com.learn.mall.common.security.permission.PermissionCheckService;
import com.learn.mall.rbac.service.MenuPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Objects;

/**
 * 本服务内直接校验权限，避免 AuthFilter 通过 Feign 回调 learn-rbac 造成线程死锁。
 */
@Service
public class LocalPermissionCheckService implements PermissionCheckService {

    @Autowired
    private MenuPermissionService menuPermissionService;

    @Override
    public boolean checkPermission(Long userId, Integer sysType, String uri, Integer isAdmin, Integer method) {
        List<String> userPermissions = menuPermissionService.listUserPermissions(
                userId, sysType, Objects.equals(isAdmin, 1));
        List<UriPermissionBO> uriPermissions = menuPermissionService.listUriPermissionInfo(sysType);
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (UriPermissionBO uriPermission : uriPermissions) {
            if (pathMatcher.match(uriPermission.getUri(), uri)
                    && Objects.equals(uriPermission.getMethod(), method)) {
                return userPermissions.contains(uriPermission.getPermission());
            }
        }
        return true;
    }
}
