package com.learn.mall.api.rbac.constant;

public enum HttpMethodEnum {

    GET(1),
    POST(2),
    PUT(3),
    DELETE(4);

    private final int value;

    HttpMethodEnum(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static HttpMethodEnum valueOfMethod(String method) {
        return HttpMethodEnum.valueOf(method.toUpperCase());
    }
}
