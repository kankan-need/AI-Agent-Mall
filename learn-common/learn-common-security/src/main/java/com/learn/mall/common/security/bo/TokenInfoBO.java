package com.learn.mall.common.security.bo;

import com.learn.mall.api.auth.bo.UserInfoInTokenBO;

public class TokenInfoBO {

    private String accessToken;
    private String refreshToken;
    private Integer expiresIn;
    private UserInfoInTokenBO userInfoInToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public UserInfoInTokenBO getUserInfoInToken() {
        return userInfoInToken;
    }

    public void setUserInfoInToken(UserInfoInTokenBO userInfoInToken) {
        this.userInfoInToken = userInfoInToken;
    }
}
