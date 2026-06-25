package com.learn.mall.auth.service;

import com.learn.mall.api.auth.bo.UserInfoInTokenBO;
import com.learn.mall.common.response.ServerResponseEntity;

public interface AuthAccountService {

    ServerResponseEntity<UserInfoInTokenBO> getUserInfoInTokenByInputUserNameAndPassword(
            String inputUserName, String password, Integer sysType);
}
