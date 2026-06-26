package com.learn.mall.product;

import com.learn.mall.common.config.EnableLearnMallCommon;
import com.learn.mall.common.security.config.EnableLearnMallSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableLearnMallCommon
@EnableLearnMallSecurity
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication(scanBasePackages = "com.learn.mall")
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}
