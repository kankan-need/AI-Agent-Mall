package com.learn.mall.common.response;

/**
 * 统一业务响应码，与 Mall4cloud 保持一致便于前端复用。
 */
public enum ResponseEnum {

    OK("00000", "ok"),
    SHOW_FAIL("A00001", ""),
    METHOD_ARGUMENT_NOT_VALID("A00002", ""),
    HTTP_MESSAGE_NOT_READABLE("A00003", "请求参数格式有误"),
    UNAUTHORIZED("A00004", "Unauthorized"),
    EXCEPTION("A00005", "服务器出了点小差"),
    SPU_NOT_EXIST("A04004", "商品不存在或已下架");

    private final String code;
    private final String msg;

    ResponseEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String value() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "ResponseEnum{code='" + code + "', msg='" + msg + "'}";
    }
}
