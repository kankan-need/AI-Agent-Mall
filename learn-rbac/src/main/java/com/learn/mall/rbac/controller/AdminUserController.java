package com.learn.mall.rbac.controller;

import com.learn.mall.api.auth.bo.UserInfoInTokenBO;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.security.AuthUserContext;
import com.learn.mall.rbac.vo.AdminUserInfoVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin/user")
@RestController
public class AdminUserController {

    @GetMapping("/info")
    public ServerResponseEntity<AdminUserInfoVO> info() {
        UserInfoInTokenBO user = AuthUserContext.get();
        AdminUserInfoVO vo = new AdminUserInfoVO();
        vo.setName(user.getUsername());
        vo.setAvatar("");
        vo.setIsAdmin(user.getIsAdmin());
        return ServerResponseEntity.success(vo);
    }
}
