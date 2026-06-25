package com.learn.mall.rbac.controller;

import com.learn.mall.api.auth.bo.UserInfoInTokenBO;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.security.AuthUserContext;
import com.learn.mall.rbac.model.Menu;
import com.learn.mall.rbac.service.MenuService;
import com.learn.mall.rbac.vo.AdminUserInfoVO;
import com.learn.mall.rbac.vo.RouteMetaVO;
import com.learn.mall.rbac.vo.RouteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequestMapping("/menu")
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/route")
    public ServerResponseEntity<List<RouteVO>> route(Integer sysType) {
        sysType = Objects.isNull(sysType) ? AuthUserContext.get().getSysType() : sysType;
        List<Menu> menus = menuService.listBySysType(sysType);
        List<RouteVO> routes = new ArrayList<>(menus.size());
        for (Menu menu : menus) {
            RouteVO route = new RouteVO();
            route.setId(menu.getMenuId());
            route.setParentId(menu.getParentId());
            route.setAlwaysShow(Objects.equals(menu.getAlwaysShow(), 1));
            route.setComponent(menu.getComponent());
            route.setHidden(Objects.equals(menu.getHidden(), 1));
            route.setName(menu.getName());
            route.setPath(menu.getPath());
            route.setRedirect(menu.getRedirect());
            route.setSeq(menu.getSeq());

            RouteMetaVO meta = new RouteMetaVO();
            meta.setActiveMenu(menu.getActiveMenu());
            meta.setAffix(Objects.equals(menu.getAffix(), 1));
            meta.setBreadcrumb(!Objects.equals(menu.getBreadcrumb(), 0));
            meta.setIcon(menu.getIcon());
            meta.setNoCache(Objects.equals(menu.getNoCache(), 1));
            meta.setTitle(menu.getTitle());
            meta.setRoles(Collections.singletonList(menu.getPermission()));
            route.setMeta(meta);
            routes.add(route);
        }
        return ServerResponseEntity.success(routes);
    }

    @GetMapping("/list_menu_ids")
    public ServerResponseEntity<List<Long>> listMenuIds() {
        UserInfoInTokenBO user = AuthUserContext.get();
        return ServerResponseEntity.success(menuService.listMenuIds(user.getUserId()));
    }
}
