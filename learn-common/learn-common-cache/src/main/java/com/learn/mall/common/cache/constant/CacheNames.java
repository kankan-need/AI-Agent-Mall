package com.learn.mall.common.cache.constant;

public final class CacheNames {

    public static final String OAUTH_PREFIX = "learn_oauth:";
    public static final String ACCESS = OAUTH_PREFIX + "token:access:";
    public static final String REFRESH_TO_ACCESS = OAUTH_PREFIX + "token:refresh_to_access:";
    public static final String UID_TO_ACCESS = OAUTH_PREFIX + "token:uid_to_access:";

    public static final String RBAC_PREFIX = "learn_rbac:";
    public static final String MENU_LIST_KEY = RBAC_PREFIX + "menu:list:";
    public static final String MENU_ID_LIST_KEY = RBAC_PREFIX + "menu:id_list:";
    public static final String PERMISSIONS_KEY = RBAC_PREFIX + "permission:permissions:";
    public static final String USER_PERMISSIONS_KEY = RBAC_PREFIX + "permission:user_permissions:";
    public static final String URI_PERMISSION_KEY = RBAC_PREFIX + "permission:uri_permissions:";

    private CacheNames() {
    }
}
