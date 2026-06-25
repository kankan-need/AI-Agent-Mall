package com.learn.mall.rbac.mapper;

import com.learn.mall.api.rbac.bo.UriPermissionBO;
import com.learn.mall.rbac.model.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper {

    List<Menu> listBySysType(@Param("sysType") Integer sysType);

    List<Long> listMenuIds(@Param("userId") Long userId);
}
