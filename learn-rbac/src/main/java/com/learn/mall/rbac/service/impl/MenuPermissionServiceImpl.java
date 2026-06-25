package com.learn.mall.rbac.service.impl;

import com.learn.mall.api.rbac.bo.UriPermissionBO;
import com.learn.mall.common.cache.constant.CacheNames;
import com.learn.mall.rbac.mapper.MenuPermissionMapper;
import com.learn.mall.rbac.service.MenuPermissionService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuPermissionServiceImpl implements MenuPermissionService {

    @Autowired
    private MenuPermissionMapper menuPermissionMapper;

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.USER_PERMISSIONS_KEY, key = "#sysType + ':' + #userId"),
            @CacheEvict(cacheNames = CacheNames.MENU_ID_LIST_KEY, key = "#userId")
    })
    public void clearUserPermissionsCache(Long userId, Integer sysType) {
    }

    @Override
    public List<String> listUserPermissions(Long userId, Integer sysType, boolean isAdmin) {
        MenuPermissionServiceImpl proxy = (MenuPermissionServiceImpl) AopContext.currentProxy();
        if (isAdmin) {
            return proxy.listAllPermission(sysType);
        }
        return proxy.listPermissionByUserIdAndSysType(userId, sysType);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.URI_PERMISSION_KEY, key = "#sysType")
    public List<UriPermissionBO> listUriPermissionInfo(Integer sysType) {
        return menuPermissionMapper.listUriPermissionInfo(sysType);
    }

    @Cacheable(cacheNames = CacheNames.PERMISSIONS_KEY, key = "#sysType")
    public List<String> listAllPermission(Integer sysType) {
        return menuPermissionMapper.listAllPermissionBySysType(sysType);
    }

    @Cacheable(cacheNames = CacheNames.USER_PERMISSIONS_KEY, key = "#sysType + ':' + #userId")
    public List<String> listPermissionByUserIdAndSysType(Long userId, Integer sysType) {
        return menuPermissionMapper.listPermissionByUserIdAndSysType(userId, sysType);
    }
}
