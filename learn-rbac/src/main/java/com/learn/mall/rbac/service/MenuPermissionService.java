package com.learn.mall.rbac.service;

import com.learn.mall.api.rbac.bo.UriPermissionBO;

import java.util.List;

public interface MenuPermissionService {

    void clearUserPermissionsCache(Long userId, Integer sysType);

    List<String> listUserPermissions(Long userId, Integer sysType, boolean isAdmin);

    List<UriPermissionBO> listUriPermissionInfo(Integer sysType);
}
