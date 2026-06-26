package com.learn.mall.common.nacos;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@AutoConfiguration
@ConditionalOnClass(NacosDiscoveryProperties.class)
@ConditionalOnProperty(name = LocalNacosDiscoveryEnvironmentPostProcessor.FORCE_KEY, havingValue = "true", matchIfMissing = true)
public class LocalNacosDiscoveryAutoConfiguration {

    @Bean
    public static BeanPostProcessor nacosDiscoveryIpBeanPostProcessor(Environment environment) {
        String ip = environment.getProperty(LocalNacosDiscoveryEnvironmentPostProcessor.IP_KEY, "127.0.0.1");
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof NacosDiscoveryProperties properties) {
                    properties.setIp(ip);
                }
                return bean;
            }
        };
    }
}
