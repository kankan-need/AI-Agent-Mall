package com.learn.mall.common.feign;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties("feign.inside")
public class FeignInsideAuthConfig {

    public static final String FEIGN_INSIDE_URL_PREFIX = "/feign";

    private String key = "learn-mall";
    private String secret = "learn-mall-secret";
    private List<String> ips = new ArrayList<>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<String> getIps() {
        return ips;
    }

    public void setIps(List<String> ips) {
        this.ips = ips != null ? ips : new ArrayList<>();
    }
}
