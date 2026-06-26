package com.learn.mall.common.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * 在 Nacos 客户端初始化前强制写入注册 IP，避免 Windows/Docker 虚拟网卡（如 10.100.x）被选中。
 */
public class LocalNacosDiscoveryEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    static final String FORCE_KEY = "learn.mall.nacos.force-local-ip";
    static final String IP_KEY = "learn.mall.nacos.discovery-ip";
    static final String NACOS_IP_PROP = "spring.cloud.nacos.discovery.ip";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (!Boolean.parseBoolean(environment.getProperty(FORCE_KEY, "true"))) {
            return;
        }
        String ip = environment.getProperty(IP_KEY, "127.0.0.1");
        Map<String, Object> props = new HashMap<>();
        props.put(NACOS_IP_PROP, ip);
        props.put(IP_KEY, ip);
        environment.getPropertySources().addFirst(new MapPropertySource("learnMallNacosDiscoveryIp", props));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
