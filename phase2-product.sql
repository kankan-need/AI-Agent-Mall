USE learn_mall;

-- ========== product tables ==========

DROP TABLE IF EXISTS shop_cart_item;
DROP TABLE IF EXISTS sku;
DROP TABLE IF EXISTS spu_detail;
DROP TABLE IF EXISTS spu;
DROP TABLE IF EXISTS category;

CREATE TABLE category (
    category_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    shop_id     BIGINT UNSIGNED NOT NULL DEFAULT 1,
    parent_id   BIGINT UNSIGNED NOT NULL DEFAULT 0,
    name        VARCHAR(50)     NOT NULL DEFAULT '',
    path        VARCHAR(255)    NOT NULL DEFAULT '',
    status      TINYINT         NOT NULL DEFAULT 1 COMMENT '1启用 0禁用 -1删除',
    icon        VARCHAR(255)    DEFAULT NULL,
    img_url     VARCHAR(255)    DEFAULT NULL,
    level       INT             NOT NULL DEFAULT 0,
    seq         INT             DEFAULT 0,
    PRIMARY KEY (category_id),
    KEY idx_shop_parent (shop_id, parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类';

CREATE TABLE spu (
    spu_id            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    create_time       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    category_id       BIGINT          NOT NULL,
    shop_id           BIGINT          NOT NULL DEFAULT 1,
    name              VARCHAR(255)    NOT NULL DEFAULT '',
    selling_point     VARCHAR(255)    DEFAULT NULL,
    main_img_url      VARCHAR(255)    DEFAULT NULL,
    img_urls          VARCHAR(1000)   DEFAULT NULL,
    price_fee         BIGINT          NOT NULL DEFAULT 0 COMMENT '售价(分)',
    market_price_fee  BIGINT          NOT NULL DEFAULT 0 COMMENT '市场价(分)',
    status            TINYINT         NOT NULL DEFAULT 0 COMMENT '1上架 0下架 -1删除',
    stock             INT UNSIGNED    NOT NULL DEFAULT 0,
    sale_num          INT UNSIGNED    NOT NULL DEFAULT 0,
    seq               INT             NOT NULL DEFAULT 0,
    PRIMARY KEY (spu_id),
    KEY idx_category (category_id),
    KEY idx_shop (shop_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品SPU';

CREATE TABLE spu_detail (
    spu_id      BIGINT   NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    detail      MEDIUMTEXT,
    PRIMARY KEY (spu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品详情';

CREATE TABLE sku (
    sku_id      BIGINT NOT NULL AUTO_INCREMENT,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    spu_id      BIGINT NOT NULL,
    price_fee   BIGINT NOT NULL DEFAULT 0,
    stock       INT UNSIGNED NOT NULL DEFAULT 0,
    status      TINYINT NOT NULL DEFAULT 1 COMMENT '1启用 0禁用',
    PRIMARY KEY (sku_id),
    UNIQUE KEY uk_spu (spu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品SKU(学习版每SPU一条)';

CREATE TABLE shop_cart_item (
    cart_item_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    create_time  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    shop_id      BIGINT          NOT NULL DEFAULT 1,
    spu_id       BIGINT UNSIGNED NOT NULL,
    sku_id       BIGINT UNSIGNED NOT NULL,
    user_id      BIGINT UNSIGNED NOT NULL,
    count        INT             NOT NULL DEFAULT 1,
    price_fee    BIGINT UNSIGNED NOT NULL,
    is_checked   TINYINT         NOT NULL DEFAULT 1,
    PRIMARY KEY (cart_item_id),
    UNIQUE KEY uk_user_shop_sku (sku_id, user_id, shop_id),
    KEY idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车';

-- ========== seed data ==========

INSERT INTO category (category_id, shop_id, parent_id, name, path, status, level, seq) VALUES
(1, 1, 0, '数码家电', '1', 1, 0, 1),
(2, 1, 0, '服饰鞋包', '2', 1, 0, 2),
(11, 1, 1, '手机通讯', '1-11', 1, 1, 1),
(12, 1, 1, '电脑办公', '1-12', 1, 1, 2),
(21, 1, 2, '男装', '2-21', 1, 1, 1);

INSERT INTO spu (spu_id, category_id, shop_id, name, selling_point, main_img_url, img_urls,
                 price_fee, market_price_fee, status, stock, sale_num, seq) VALUES
(1, 11, 1, '学习版智能手机', 'Phase2 示例商品', 'https://img.yzcdn.cn/vant/cat.jpeg',
 'https://img.yzcdn.cn/vant/cat.jpeg', 299900, 399900, 1, 100, 12, 1),
(2, 12, 1, '轻薄笔记本电脑', '办公学习必备', 'https://img.yzcdn.cn/vant/apple-1.jpg',
 'https://img.yzcdn.cn/vant/apple-1.jpg', 499900, 599900, 1, 50, 5, 2),
(3, 21, 1, '休闲纯棉T恤', '舒适透气', 'https://img.yzcdn.cn/vant/apple-2.jpg',
 'https://img.yzcdn.cn/vant/apple-2.jpg', 9900, 19900, 1, 200, 30, 3);

INSERT INTO spu_detail (spu_id, detail) VALUES
(1, '<p>学习版商城 Phase2 示例手机，用于演示商品详情页。</p>'),
(2, '<p>学习版商城 Phase2 示例笔记本。</p>'),
(3, '<p>学习版商城 Phase2 示例T恤。</p>');

INSERT INTO sku (sku_id, spu_id, price_fee, stock, status) VALUES
(1, 1, 299900, 100, 1),
(2, 2, 499900, 50, 1),
(3, 3, 9900, 200, 1);

-- C端测试用户 user1 / 123456 (sysType=0)
INSERT INTO auth_account (uid, username, password, create_ip, status, sys_type, user_id, tenant_id, is_admin)
VALUES (2, 'user1', '$2a$10$EiwfzqsAVUtuJ0Ry5YPMPOeyc/4shzSUcqMBRInKTijzir48LLkM.', '127.0.0.1', 1, 0, 2, 0, 0)
ON DUPLICATE KEY UPDATE username = VALUES(username);

-- ========== admin menus ==========

INSERT INTO menu (menu_id, parent_id, biz_type, path, component, redirect, hidden, name, title, icon, seq) VALUES
(5, 1, 2, 'product', 'Layout', '/product/spu/index', 0, 'Product', '商品管理', 'goods', 3),
(6, 5, 2, 'category', 'product/category/index', NULL, 0, 'ProductCategory', '分类管理', 'menu', 1),
(7, 5, 2, 'spu', 'product/spu/index', NULL, 0, 'ProductSpu', '商品列表', 'list', 2),
(8, 5, 2, 'edit', 'product/spu/edit', NULL, 1, 'ProductSpuEdit', '编辑商品', 'edit', 3);

INSERT INTO role_menu (role_id, menu_id) VALUES
(1, 5), (1, 6), (1, 7), (1, 8);
