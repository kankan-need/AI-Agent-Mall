package com.learn.mall.common.util;

import java.util.regex.Pattern;

public final class PrincipalUtil {

    private static final Pattern USER_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{4,30}$");
    private static final Pattern SIMPLE_CHAR_PATTERN = Pattern.compile("^[a-z0-9]+$");

    private PrincipalUtil() {
    }

    public static boolean isUserName(String value) {
        return value != null && USER_NAME_PATTERN.matcher(value).matches();
    }

    public static boolean isSimpleChar(String value) {
        return value != null && SIMPLE_CHAR_PATTERN.matcher(value).matches();
    }
}
