package com.learn.mall.rbac.service.impl;

import com.learn.mall.common.cache.constant.CacheNames;
import com.learn.mall.rbac.mapper.MenuMapper;
import com.learn.mall.rbac.model.Menu;
import com.learn.mall.rbac.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    @Cacheable(cacheNames = CacheNames.MENU_LIST_KEY, key = "#sysType")
    public List<Menu> listBySysType(Integer sysType) {
        return menuMapper.listBySysType(sysType);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.MENU_ID_LIST_KEY, key = "#userId")
    public List<Long> listMenuIds(Long userId) {
        return menuMapper.listMenuIds(userId);
    }
}
