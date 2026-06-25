package com.learn.mall.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AuthenticationDTO {

    @NotBlank(message = "用户名不能为空")
    private String principal;

    @NotBlank(message = "密码不能为空")
    private String credentials;

    @NotNull(message = "系统类型不能为空")
    private Integer sysType;

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public Integer getSysType() {
        return sysType;
    }

    public void setSysType(Integer sysType) {
        this.sysType = sysType;
    }
}
