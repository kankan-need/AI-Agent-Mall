package com.learn.mall.agent;

import com.learn.mall.common.config.EnableLearnMallCommon;
import com.learn.mall.common.security.config.EnableLearnMallSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableLearnMallCommon
@EnableLearnMallSecurity
@SpringBootApplication(scanBasePackages = "com.learn.mall")
public class AgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgentApplication.class, args);
    }
}
