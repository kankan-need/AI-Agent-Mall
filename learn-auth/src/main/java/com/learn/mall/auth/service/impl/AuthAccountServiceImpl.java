package com.learn.mall.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.learn.mall.api.auth.bo.UserInfoInTokenBO;
import com.learn.mall.auth.mapper.AuthAccountMapper;
import com.learn.mall.auth.service.AuthAccountService;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.security.bo.AuthAccountInVerifyBO;
import com.learn.mall.common.util.BeanUtil;
import com.learn.mall.common.util.PrincipalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthAccountServiceImpl implements AuthAccountService {

    private static final String USER_NOT_FOUND_SECRET = "USER_NOT_FOUND_SECRET";
    private static String userNotFoundEncodedPassword;

    @Autowired
    private AuthAccountMapper authAccountMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ServerResponseEntity<UserInfoInTokenBO> getUserInfoInTokenByInputUserNameAndPassword(
            String inputUserName, String password, Integer sysType) {
        if (StrUtil.isBlank(inputUserName)) {
            return ServerResponseEntity.showFailMsg("用户名不能为空");
        }
        if (StrUtil.isBlank(password)) {
            return ServerResponseEntity.showFailMsg("密码不能为空");
        }
        if (!PrincipalUtil.isUserName(inputUserName)) {
            return ServerResponseEntity.showFailMsg("请输入正确的用户名");
        }

        AuthAccountInVerifyBO account = authAccountMapper.getAuthAccountInVerifyByInputUserName(inputUserName, sysType);
        if (account == null) {
            prepareTimingAttackProtection();
            mitigateAgainstTimingAttack(password);
            return ServerResponseEntity.showFailMsg("用户名或密码不正确");
        }
        if (!passwordEncoder.matches(password, account.getPassword())) {
            return ServerResponseEntity.showFailMsg("用户名或密码不正确");
        }
        return ServerResponseEntity.success(BeanUtil.map(account, UserInfoInTokenBO.class));
    }

    private void prepareTimingAttackProtection() {
        if (userNotFoundEncodedPassword == null) {
            userNotFoundEncodedPassword = passwordEncoder.encode(USER_NOT_FOUND_SECRET);
        }
    }

    private void mitigateAgainstTimingAttack(String presentedPassword) {
        if (presentedPassword != null) {
            passwordEncoder.matches(presentedPassword, userNotFoundEncodedPassword);
        }
    }
}
