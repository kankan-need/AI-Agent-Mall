package com.learn.mall.common.security;

import com.learn.mall.api.auth.bo.UserInfoInTokenBO;

public final class AuthUserContext {

    private static final ThreadLocal<UserInfoInTokenBO> USER = new ThreadLocal<>();

    private AuthUserContext() {
    }

    public static void set(UserInfoInTokenBO user) {
        USER.set(user);
    }

    public static UserInfoInTokenBO get() {
        return USER.get();
    }

    public static void clean() {
        USER.remove();
    }
}
