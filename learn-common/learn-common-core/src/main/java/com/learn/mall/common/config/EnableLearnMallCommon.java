package com.learn.mall.common.config;

import com.learn.mall.common.handler.DefaultExceptionHandlerConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 引入公共模块的全局异常处理等基础配置。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DefaultExceptionHandlerConfig.class)
public @interface EnableLearnMallCommon {
}
