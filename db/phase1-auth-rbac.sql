USE learn_mall;

-- auth
DROP TABLE IF EXISTS auth_account;
CREATE TABLE auth_account (
    uid         BIGINT UNSIGNED NOT NULL COMMENT '全平台用户唯一id',
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    username    VARCHAR(30)     NOT NULL DEFAULT '',
    password    VARCHAR(64)     NOT NULL DEFAULT '',
    create_ip   VARCHAR(15)     NOT NULL DEFAULT '',
    status      TINYINT         NOT NULL COMMENT '1启用 0禁用',
    sys_type    TINYINT         NOT NULL COMMENT '2平台端',
    user_id     BIGINT          NOT NULL,
    tenant_id   BIGINT          DEFAULT 0,
    is_admin    TINYINT         DEFAULT 0,
    PRIMARY KEY (uid),
    UNIQUE KEY uk_usertype_userid (sys_type, user_id),
    KEY idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='统一账户';

-- rbac
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS role_menu;
DROP TABLE IF EXISTS menu_permission;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS menu;

CREATE TABLE menu (
    menu_id     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    parent_id   BIGINT UNSIGNED NOT NULL DEFAULT 0,
    biz_type    TINYINT         NOT NULL COMMENT '2平台端',
    permission  VARCHAR(255)    DEFAULT NULL,
    path        VARCHAR(255)    DEFAULT NULL,
    component   VARCHAR(255)    DEFAULT NULL,
    redirect    VARCHAR(255)    DEFAULT NULL,
    always_show TINYINT         DEFAULT NULL,
    hidden      TINYINT         DEFAULT 0,
    name        VARCHAR(32)     DEFAULT NULL,
    title       VARCHAR(32)     DEFAULT NULL,
    icon        VARCHAR(32)     DEFAULT NULL,
    no_cache    TINYINT         DEFAULT NULL,
    breadcrumb  TINYINT         DEFAULT 1,
    affix       TINYINT         DEFAULT NULL,
    active_menu VARCHAR(255)    DEFAULT NULL,
    seq         INT             DEFAULT 0,
    PRIMARY KEY (menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单';

CREATE TABLE menu_permission (
    menu_permission_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    create_time        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    menu_id            BIGINT          NOT NULL,
    biz_type           TINYINT         NOT NULL,
    permission         VARCHAR(255)    NOT NULL,
    name               VARCHAR(32)     NOT NULL,
    uri                VARCHAR(255)    NOT NULL,
    method             TINYINT         NOT NULL COMMENT '1GET 2POST 3PUT 4DELETE',
    PRIMARY KEY (menu_permission_id),
    UNIQUE KEY uk_permission (permission, biz_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限';

CREATE TABLE role (
    role_id     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    role_name   VARCHAR(32)     NOT NULL,
    biz_type    TINYINT         NOT NULL,
    tenant_id   BIGINT          DEFAULT 0,
    PRIMARY KEY (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色';

CREATE TABLE role_menu (
    id                 BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    create_time        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    role_id            BIGINT UNSIGNED NOT NULL,
    menu_id            BIGINT UNSIGNED DEFAULT NULL,
    menu_permission_id BIGINT UNSIGNED DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_menu (role_id, menu_id, menu_permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单';

CREATE TABLE user_role (
    id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id     BIGINT          NOT NULL,
    role_id     BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色';

-- 默认管理员 admin / 123456
INSERT INTO auth_account (uid, username, password, create_ip, status, sys_type, user_id, tenant_id, is_admin)
VALUES (1, 'admin', '$2a$10$EiwfzqsAVUtuJ0Ry5YPMPOeyc/4shzSUcqMBRInKTijzir48LLkM.', '127.0.0.1', 1, 2, 1, 0, 1);

INSERT INTO role (role_id, role_name, biz_type, tenant_id) VALUES (1, '超级管理员', 2, 0);

INSERT INTO menu (menu_id, parent_id, biz_type, path, component, redirect, hidden, name, title, icon, seq) VALUES
(1, 0, 2, '/', 'Layout', '/dashboard/index', 0, 'Root', '根路由', 'menu', 0),
(2, 1, 2, 'dashboard', 'Layout', '/dashboard/index', 0, 'Dashboard', '控制台', 'odometer', 1),
(3, 2, 2, 'index', 'dashboard/index', NULL, 0, 'DashboardIndex', '首页', 'home-filled', 1),
(4, 1, 2, 'demo', 'demo/ping', NULL, 0, 'DemoPing', '服务探活', 'connection', 2);

INSERT INTO role_menu (role_id, menu_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4);

INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
