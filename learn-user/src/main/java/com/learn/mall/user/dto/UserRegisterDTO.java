package com.learn.mall.user.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRegisterDTO {

    @NotBlank(message = "密码不能为空")
    private String password;
    private String img;
    private String nickName;
    @NotBlank(message = "用户名不能为空")
    private String userName;
    private Long userId;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
