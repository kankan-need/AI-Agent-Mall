package com.learn.mall.common.exception;

import com.learn.mall.common.response.ResponseEnum;

import java.io.Serial;

/**
 * 业务异常，由全局异常处理器转换为统一响应。
 */
public class LearnMallException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Object object;
    private final ResponseEnum responseEnum;

    public LearnMallException(String msg) {
        super(msg);
        this.object = null;
        this.responseEnum = null;
    }

    public LearnMallException(ResponseEnum responseEnum) {
        super(responseEnum.getMsg());
        this.responseEnum = responseEnum;
        this.object = null;
    }

    public LearnMallException(ResponseEnum responseEnum, Object object) {
        super(responseEnum.getMsg());
        this.responseEnum = responseEnum;
        this.object = object;
    }

    public LearnMallException(String msg, Throwable cause) {
        super(msg, cause);
        this.object = null;
        this.responseEnum = null;
    }

    public Object getObject() {
        return object;
    }

    public ResponseEnum getResponseEnum() {
        return responseEnum;
    }
}
