package com.learn.mall.demo.controller;

import com.learn.mall.common.response.ServerResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class PingController {

    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping("/ping")
    public ServerResponseEntity<Map<String, String>> ping() {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("service", applicationName);
        data.put("status", "ok");
        data.put("phase", "0");
        return ServerResponseEntity.success(data);
    }
}
