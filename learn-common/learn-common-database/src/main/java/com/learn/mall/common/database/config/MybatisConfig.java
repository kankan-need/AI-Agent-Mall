package com.learn.mall.common.database.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.learn.mall.**.mapper")
public class MybatisConfig {
}
