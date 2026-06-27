package com.learn.mall.user.controller.app;

import cn.hutool.core.util.StrUtil;
import com.learn.mall.api.auth.bo.UserInfoInTokenBO;
import com.learn.mall.api.auth.constant.SysTypeEnum;
import com.learn.mall.api.auth.feign.AccountFeignClient;
import com.learn.mall.api.auth.vo.TokenInfoVO;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.user.dto.UserRegisterDTO;
import com.learn.mall.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ua/user/register")
public class UserRegisterController {

    @Autowired
    private UserService userService;
    @Autowired
    private AccountFeignClient accountFeignClient;

    @PostMapping
    public ServerResponseEntity<TokenInfoVO> register(@Valid @RequestBody UserRegisterDTO param) {
        if (StrUtil.isBlank(param.getNickName())) {
            param.setNickName(param.getUserName());
        }
        Long uid = userService.save(param);

        UserInfoInTokenBO userInfoInTokenBO = new UserInfoInTokenBO();
        userInfoInTokenBO.setUid(uid);
        userInfoInTokenBO.setUserId(uid);
        userInfoInTokenBO.setSysType(SysTypeEnum.ORDINARY.value());
        userInfoInTokenBO.setIsAdmin(0);
        userInfoInTokenBO.setUsername(param.getUserName());
        return accountFeignClient.storeTokenAndGetVo(userInfoInTokenBO);
    }
}
