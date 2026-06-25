package com.learn.mall.api.rbac.bo;

import java.io.Serial;
import java.io.Serializable;

public class UriPermissionBO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String uri;
    private Integer method;
    private String permission;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
