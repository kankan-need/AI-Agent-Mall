package com.learn.mall.common.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 统一 API 响应体。
 */
public class ServerResponseEntity<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(ServerResponseEntity.class);

    private String code;
    private String msg;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return Objects.equals(ResponseEnum.OK.value(), this.code);
    }

    @Override
    public String toString() {
        return "ServerResponseEntity{code=" + code + ", msg='" + msg + "', data=" + data + '}';
    }

    public static <T> ServerResponseEntity<T> success(T data) {
        ServerResponseEntity<T> entity = new ServerResponseEntity<>();
        entity.setData(data);
        entity.setCode(ResponseEnum.OK.value());
        entity.setMsg(ResponseEnum.OK.getMsg());
        return entity;
    }

    public static <T> ServerResponseEntity<T> success() {
        return success(null);
    }

    public static <T> ServerResponseEntity<T> showFailMsg(String msg) {
        log.error(msg);
        ServerResponseEntity<T> entity = new ServerResponseEntity<>();
        entity.setMsg(msg);
        entity.setCode(ResponseEnum.SHOW_FAIL.value());
        return entity;
    }

    public static <T> ServerResponseEntity<T> fail(ResponseEnum responseEnum) {
        log.error(responseEnum.toString());
        ServerResponseEntity<T> entity = new ServerResponseEntity<>();
        entity.setMsg(responseEnum.getMsg());
        entity.setCode(responseEnum.value());
        return entity;
    }

    public static <T> ServerResponseEntity<T> fail(ResponseEnum responseEnum, T data) {
        log.error(responseEnum.toString());
        ServerResponseEntity<T> entity = new ServerResponseEntity<>();
        entity.setMsg(responseEnum.getMsg());
        entity.setCode(responseEnum.value());
        entity.setData(data);
        return entity;
    }

    public static <T> ServerResponseEntity<T> transform(ServerResponseEntity<?> oldEntity) {
        ServerResponseEntity<T> entity = new ServerResponseEntity<>();
        entity.setMsg(oldEntity.getMsg());
        entity.setCode(oldEntity.getCode());
        return entity;
    }
}
