package com.learn.mall.common.security.permission;

public interface PermissionCheckService {

    boolean checkPermission(Long userId, Integer sysType, String uri, Integer isAdmin, Integer method);
}
