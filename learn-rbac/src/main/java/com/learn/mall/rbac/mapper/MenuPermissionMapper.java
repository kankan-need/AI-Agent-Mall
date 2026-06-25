package com.learn.mall.rbac.mapper;

import com.learn.mall.api.rbac.bo.UriPermissionBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuPermissionMapper {

    List<String> listAllPermissionBySysType(@Param("sysType") Integer sysType);

    List<String> listPermissionByUserIdAndSysType(@Param("userId") Long userId, @Param("sysType") Integer sysType);

    List<UriPermissionBO> listUriPermissionInfo(@Param("sysType") Integer sysType);
}
