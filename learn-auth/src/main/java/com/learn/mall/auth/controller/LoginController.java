package com.learn.mall.auth.controller;

import com.learn.mall.api.auth.bo.UserInfoInTokenBO;
import com.learn.mall.api.auth.vo.TokenInfoVO;
import com.learn.mall.api.rbac.dto.ClearUserPermissionsCacheDTO;
import com.learn.mall.api.rbac.feign.PermissionFeignClient;
import com.learn.mall.auth.dto.AuthenticationDTO;
import com.learn.mall.auth.manager.TokenStore;
import com.learn.mall.auth.service.AuthAccountService;
import com.learn.mall.common.response.ResponseEnum;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.security.AuthUserContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private AuthAccountService authAccountService;
    @Autowired
    private PermissionFeignClient permissionFeignClient;

    @PostMapping("/ua/login")
    public ServerResponseEntity<TokenInfoVO> login(@Valid @RequestBody AuthenticationDTO authenticationDTO) {
        ServerResponseEntity<UserInfoInTokenBO> userInfoResponse = authAccountService
                .getUserInfoInTokenByInputUserNameAndPassword(
                        authenticationDTO.getPrincipal(),
                        authenticationDTO.getCredentials(),
                        authenticationDTO.getSysType());
        if (!userInfoResponse.isSuccess()) {
            return ServerResponseEntity.transform(userInfoResponse);
        }

        UserInfoInTokenBO data = userInfoResponse.getData();
        ClearUserPermissionsCacheDTO clearDto = new ClearUserPermissionsCacheDTO();
        clearDto.setSysType(data.getSysType());
        clearDto.setUserId(data.getUserId());
        ServerResponseEntity<Void> clearResponse = permissionFeignClient.clearUserPermissionsCache(clearDto);
        if (!clearResponse.isSuccess()) {
            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
        }
        return ServerResponseEntity.success(tokenStore.storeAndGetVo(data));
    }

    @PostMapping("/login_out")
    public ServerResponseEntity<Void> loginOut() {
        UserInfoInTokenBO userInfoInToken = AuthUserContext.get();
        ClearUserPermissionsCacheDTO clearDto = new ClearUserPermissionsCacheDTO();
        clearDto.setSysType(userInfoInToken.getSysType());
        clearDto.setUserId(userInfoInToken.getUserId());
        permissionFeignClient.clearUserPermissionsCache(clearDto);
        tokenStore.deleteAllToken(userInfoInToken.getSysType().toString(), userInfoInToken.getUid());
        return ServerResponseEntity.success();
    }
}
