package com.learn.mall.rbac.service;

import com.learn.mall.rbac.model.Menu;

import java.util.List;

public interface MenuService {

    List<Menu> listBySysType(Integer sysType);

    List<Long> listMenuIds(Long userId);
}
