package com.learn.mall.order;

import com.learn.mall.common.config.EnableLearnMallCommon;
import com.learn.mall.common.security.config.EnableLearnMallSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableLearnMallCommon
@EnableLearnMallSecurity
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication(scanBasePackages = "com.learn.mall")
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
