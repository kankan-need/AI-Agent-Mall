package com.learn.mall.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

/**
 * Jackson 工具；Redis 序列化需使用带类型信息的 {@link #getRedisObjectMapper()}。
 */
public final class Json {

    private static final ObjectMapper REDIS_OBJECT_MAPPER = newBaseBuilder()
            .activateDefaultTyping(
                    BasicPolymorphicTypeValidator.builder()
                            .allowIfBaseType(Object.class)
                            .build(),
                    DefaultTyping.NON_FINAL,
                    JsonTypeInfo.As.WRAPPER_ARRAY)
            .build();

    private Json() {
    }

    public static JsonMapper.Builder newBaseBuilder() {
        return JsonMapper.builder()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_SELF_REFERENCES)
                .enable(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL)
                .disable(EnumFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
                .changeDefaultVisibility(vc -> vc
                        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
    }

    public static ObjectMapper getRedisObjectMapper() {
        return REDIS_OBJECT_MAPPER;
    }
}
