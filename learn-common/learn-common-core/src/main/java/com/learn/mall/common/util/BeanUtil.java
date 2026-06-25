package com.learn.mall.common.util;

import org.springframework.beans.BeanUtils;

public final class BeanUtil {

    private BeanUtil() {
    }

    public static <T> T map(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("bean copy failed", e);
        }
    }
}
