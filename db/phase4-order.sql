USE learn_mall;

DROP TABLE IF EXISTS pay_info;
DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS order_addr;

CREATE TABLE order_addr (
    order_addr_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id         BIGINT          DEFAULT NULL,
    consignee       VARCHAR(50)     DEFAULT NULL,
    province_id     BIGINT          DEFAULT NULL,
    province        VARCHAR(100)    DEFAULT NULL,
    city_id         BIGINT          DEFAULT NULL,
    city            VARCHAR(20)     DEFAULT NULL,
    area_id         BIGINT          DEFAULT NULL,
    area            VARCHAR(20)     DEFAULT NULL,
    addr            VARCHAR(1000)   DEFAULT NULL,
    post_code       VARCHAR(15)     DEFAULT NULL,
    mobile          VARCHAR(20)     DEFAULT NULL,
    PRIMARY KEY (order_addr_id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单地址快照';

CREATE TABLE `order` (
    order_id       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    create_time    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    shop_id        BIGINT          NOT NULL DEFAULT 1,
    user_id        BIGINT          NOT NULL,
    shop_name      VARCHAR(255)    DEFAULT '学习商城',
    total          BIGINT          NOT NULL COMMENT '订单金额(分)',
    status         TINYINT         NOT NULL DEFAULT 1 COMMENT '1待付 2已付 5完成 6关闭',
    all_count      INT             DEFAULT 0,
    pay_time       DATETIME        DEFAULT NULL,
    cancel_time    DATETIME        DEFAULT NULL,
    is_payed       TINYINT         NOT NULL DEFAULT 0,
    close_type     TINYINT         DEFAULT NULL COMMENT '1超时 4用户取消',
    order_addr_id  BIGINT          DEFAULT NULL,
    PRIMARY KEY (order_id),
    KEY idx_user_id (user_id),
    KEY idx_status_create (status, create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单';

CREATE TABLE order_item (
    order_item_id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    shop_id         BIGINT          NOT NULL DEFAULT 1,
    order_id        BIGINT          NOT NULL,
    category_id     BIGINT          DEFAULT NULL,
    spu_id          BIGINT UNSIGNED NOT NULL,
    sku_id          BIGINT UNSIGNED NOT NULL,
    user_id         BIGINT          NOT NULL,
    count           INT             NOT NULL DEFAULT 1,
    spu_name        VARCHAR(120)    DEFAULT '',
    pic             VARCHAR(255)    DEFAULT '',
    price           BIGINT          NOT NULL COMMENT '单价(分)',
    spu_total_amount BIGINT         NOT NULL COMMENT '行小计(分)',
    PRIMARY KEY (order_item_id),
    KEY idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单项';

CREATE TABLE pay_info (
    pay_id       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    create_time  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id      BIGINT          DEFAULT NULL,
    order_ids    VARCHAR(255)    DEFAULT NULL,
    biz_pay_no   VARCHAR(36)     DEFAULT NULL,
    pay_status   TINYINT         NOT NULL DEFAULT 0 COMMENT '0未付 1已付',
    pay_amount   BIGINT          DEFAULT NULL,
    confirm_time DATETIME        DEFAULT NULL,
    PRIMARY KEY (pay_id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录';

-- admin 菜单
INSERT INTO menu (menu_id, parent_id, biz_type, path, component, redirect, hidden, name, title, icon, seq) VALUES
(9, 1, 2, 'order', 'Layout', '/order/index', 0, 'Order', '订单管理', 'list', 4),
(10, 9, 2, 'index', 'order/index', NULL, 0, 'OrderList', '订单列表', 'list', 1),
(11, 9, 2, 'detail', 'order/detail', NULL, 1, 'OrderDetail', '订单详情', 'view', 2);

INSERT INTO role_menu (role_id, menu_id) VALUES (1, 9), (1, 10), (1, 11);
