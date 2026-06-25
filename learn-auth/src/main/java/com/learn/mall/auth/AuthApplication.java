package com.learn.mall.auth;

import com.learn.mall.common.config.EnableLearnMallCommon;
import com.learn.mall.common.security.config.EnableLearnMallSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableLearnMallCommon
@EnableLearnMallSecurity
@SpringBootApplication(scanBasePackages = "com.learn.mall")
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
