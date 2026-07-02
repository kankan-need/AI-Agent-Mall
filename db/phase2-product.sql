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
(3, 1, 0, '食品饮料', '3', 1, 0, 3),
(4, 1, 0, '家居生活', '4', 1, 0, 4),
(11, 1, 1, '手机通讯', '1-11', 1, 1, 1),
(12, 1, 1, '电脑办公', '1-12', 1, 1, 2),
(13, 1, 1, '数码配件', '1-13', 1, 1, 3),
(21, 1, 2, '男装', '2-21', 1, 1, 1),
(22, 1, 2, '女装', '2-22', 1, 1, 2),
(23, 1, 2, '鞋靴', '2-23', 1, 1, 3),
(31, 1, 3, '休闲零食', '3-31', 1, 1, 1),
(32, 1, 3, '饮料冲调', '3-32', 1, 1, 2),
(41, 1, 4, '厨房用品', '4-41', 1, 1, 1),
(42, 1, 4, '收纳清洁', '4-42', 1, 1, 2);

INSERT INTO spu (spu_id, category_id, shop_id, name, selling_point, main_img_url, img_urls,
                 price_fee, market_price_fee, status, stock, sale_num, seq) VALUES
(1, 11, 1, '学习版智能手机', 'Phase2 示例商品', 'https://img.yzcdn.cn/vant/cat.jpeg',
 'https://img.yzcdn.cn/vant/cat.jpeg', 299900, 399900, 1, 100, 12, 1),
(2, 12, 1, '轻薄笔记本电脑', '办公学习必备', 'https://img.yzcdn.cn/vant/apple-1.jpg',
 'https://img.yzcdn.cn/vant/apple-1.jpg', 499900, 599900, 1, 50, 5, 2),
(3, 21, 1, '休闲纯棉T恤', '舒适透气', 'https://img.yzcdn.cn/vant/apple-2.jpg',
 'https://img.yzcdn.cn/vant/apple-2.jpg', 9900, 19900, 1, 200, 30, 3),
(4,  11, 1, '5G 旗舰智能手机 Pro',     '120Hz 高刷屏 · 一亿像素',     'https://img.yzcdn.cn/vant/apple-3.jpg', 'https://img.yzcdn.cn/vant/apple-3.jpg,https://img.yzcdn.cn/vant/apple-4.jpg', 459900, 529900, 1, 80,  28, 4),
(5,  11, 1, '入门款学生手机',           '长续航 · 大内存',             'https://img.yzcdn.cn/vant/apple-4.jpg', 'https://img.yzcdn.cn/vant/apple-4.jpg', 129900, 159900, 1, 150, 56, 5),
(6,  12, 1, '14英寸轻薄办公本',         '16G内存 · 512G固态',          'https://img.yzcdn.cn/vant/tree.jpg',    'https://img.yzcdn.cn/vant/tree.jpg',    399900, 459900, 1, 60,  18, 6),
(7,  12, 1, '游戏性能笔记本',           '独显直连 · 高刷电竞屏',       'https://img.yzcdn.cn/vant/leaf.jpg',    'https://img.yzcdn.cn/vant/leaf.jpg',    699900, 799900, 1, 35,  9,  7),
(8,  13, 1, '无线蓝牙耳机',             '降噪长续航 · 低延迟',         'https://img.yzcdn.cn/vant/cat.jpeg',    'https://img.yzcdn.cn/vant/cat.jpeg',    19900,  29900,  1, 300, 120, 8),
(9,  13, 1, '快充移动电源 20000mAh',    '双向快充 · 多口输出',         'https://img.yzcdn.cn/vant/apple-1.jpg', 'https://img.yzcdn.cn/vant/apple-1.jpg', 12900,  19900,  1, 500, 88,  9),
(10, 13, 1, '机械键盘 87 键',           '青轴手感 · RGB 背光',         'https://img.yzcdn.cn/vant/apple-2.jpg', 'https://img.yzcdn.cn/vant/apple-2.jpg', 25900,  35900,  1, 120, 45,  10),
(11, 21, 1, '商务休闲 Polo 衫',         '透气面料 · 多色可选',         'https://img.yzcdn.cn/vant/apple-3.jpg', 'https://img.yzcdn.cn/vant/apple-3.jpg', 15900,  25900,  1, 180, 62,  11),
(12, 21, 1, '秋冬加厚卫衣',             '加绒保暖 · 宽松版型',         'https://img.yzcdn.cn/vant/apple-4.jpg', 'https://img.yzcdn.cn/vant/apple-4.jpg', 18900,  28900,  1, 160, 41,  12),
(13, 22, 1, '法式复古连衣裙',           '收腰显瘦 · 通勤约会',         'https://img.yzcdn.cn/vant/tree.jpg',    'https://img.yzcdn.cn/vant/tree.jpg',    25900,  39900,  1, 90,  33,  13),
(14, 22, 1, '针织开衫外套',             '柔软亲肤 · 百搭款',           'https://img.yzcdn.cn/vant/leaf.jpg',    'https://img.yzcdn.cn/vant/leaf.jpg',    21900,  32900,  1, 110, 27,  14),
(15, 23, 1, '运动跑步鞋',               '轻便缓震 · 透气网面',         'https://img.yzcdn.cn/vant/cat.jpeg',    'https://img.yzcdn.cn/vant/cat.jpeg',    29900,  45900,  1, 200, 75,  15),
(16, 23, 1, '真皮商务皮鞋',             '软底舒适 · 正装百搭',         'https://img.yzcdn.cn/vant/apple-1.jpg', 'https://img.yzcdn.cn/vant/apple-1.jpg', 35900,  49900,  1, 80,  19,  16),
(17, 31, 1, '每日坚果礼盒 750g',        '7种坚果 · 独立小包',          'https://img.yzcdn.cn/vant/apple-2.jpg', 'https://img.yzcdn.cn/vant/apple-2.jpg', 8900,   12900,  1, 400, 210, 17),
(18, 31, 1, '手工曲奇饼干 500g',        '黄油香浓 · 酥脆可口',         'https://img.yzcdn.cn/vant/apple-3.jpg', 'https://img.yzcdn.cn/vant/apple-3.jpg', 4900,   7900,   1, 600, 156, 18),
(19, 32, 1, '挂耳咖啡组合装 24包',      '中度烘焙 · 醇香浓郁',         'https://img.yzcdn.cn/vant/apple-4.jpg', 'https://img.yzcdn.cn/vant/apple-4.jpg', 6900,   9900,   1, 350, 98,  19),
(20, 32, 1, '鲜榨橙汁 1L*6 盒',         '无添加 · 冷藏直达',           'https://img.yzcdn.cn/vant/tree.jpg',    'https://img.yzcdn.cn/vant/tree.jpg',    5900,   8900,   1, 280, 67,  20),
(21, 41, 1, '不粘炒锅 32cm',            '少油烟 · 电磁炉通用',         'https://img.yzcdn.cn/vant/leaf.jpg',    'https://img.yzcdn.cn/vant/leaf.jpg',    15900,  22900,  1, 150, 38,  21),
(22, 41, 1, '保温杯 500ml',             '24小时保温 · 316不锈钢',      'https://img.yzcdn.cn/vant/cat.jpeg',    'https://img.yzcdn.cn/vant/cat.jpeg',    7900,   11900,  1, 420, 142, 22),
(23, 42, 1, '真空压缩收纳袋 8件套',     '换季收纳 · 防潮防霉',         'https://img.yzcdn.cn/vant/apple-1.jpg', 'https://img.yzcdn.cn/vant/apple-1.jpg', 3900,   6900,   1, 500, 89,  23),
(24, 42, 1, '免手洗平板拖把',           '加大面板 · 强力去污',         'https://img.yzcdn.cn/vant/apple-2.jpg', 'https://img.yzcdn.cn/vant/apple-2.jpg', 4900,   7900,   1, 380, 54,  24);

INSERT INTO spu_detail (spu_id, detail) VALUES
(1, '<p>学习版商城 Phase2 示例手机，用于演示商品详情页。</p>'),
(2, '<p>学习版商城 Phase2 示例笔记本。</p>'),
(3, '<p>学习版商城 Phase2 示例T恤。</p>'),
(4,  '<p><strong>5G 旗舰智能手机 Pro</strong></p><p>6.7 英寸 OLED 屏幕，支持 120Hz 自适应刷新率；后置三摄系统，夜景拍摄更清晰。</p>'),
(5,  '<p><strong>入门款学生手机</strong></p><p>适合学生与长辈使用，系统简洁流畅，续航可达 1.5 天。</p>'),
(6,  '<p><strong>14英寸轻薄办公本</strong></p><p>全金属机身，重量约 1.3kg，适合办公与学习。</p>'),
(7,  '<p><strong>游戏性能笔记本</strong></p><p>高性能处理器 + 独立显卡，畅玩主流游戏。</p>'),
(8,  '<p><strong>无线蓝牙耳机</strong></p><p>主动降噪，单次续航 8 小时。</p>'),
(9,  '<p><strong>快充移动电源</strong></p><p>20000mAh 大容量，支持 PD 快充。</p>'),
(10, '<p><strong>机械键盘 87 键</strong></p><p>热插拔轴体，全键无冲。</p>'),
(11, '<p><strong>商务休闲 Polo 衫</strong></p><p>精选棉混纺面料，抗皱易打理。</p>'),
(12, '<p><strong>秋冬加厚卫衣</strong></p><p>内里加绒，保暖舒适。</p>'),
(13, '<p><strong>法式复古连衣裙</strong></p><p>A 字版型修饰身形，通勤约会皆宜。</p>'),
(14, '<p><strong>针织开衫外套</strong></p><p>软糯针织面料，日常百搭。</p>'),
(15, '<p><strong>运动跑步鞋</strong></p><p>轻量化鞋身，透气网面。</p>'),
(16, '<p><strong>真皮商务皮鞋</strong></p><p>头层牛皮鞋面，橡胶防滑大底。</p>'),
(17, '<p><strong>每日坚果礼盒</strong></p><p>7 种坚果，独立小包装。</p>'),
(18, '<p><strong>手工曲奇饼干</strong></p><p>进口黄油制作，奶香浓郁。</p>'),
(19, '<p><strong>挂耳咖啡组合装</strong></p><p>精选阿拉比卡咖啡豆，中度烘焙。</p>'),
(20, '<p><strong>鲜榨橙汁</strong></p><p>非浓缩还原，建议冷藏饮用。</p>'),
(21, '<p><strong>不粘炒锅</strong></p><p>多层复合锅底，导热均匀。</p>'),
(22, '<p><strong>保温杯 500ml</strong></p><p>316 不锈钢内胆，保温 24 小时。</p>'),
(23, '<p><strong>真空压缩收纳袋</strong></p><p>8 件套，节省 70% 收纳空间。</p>'),
(24, '<p><strong>免手洗平板拖把</strong></p><p>洗脱一体设计，全程不脏手。</p>');

INSERT INTO sku (sku_id, spu_id, price_fee, stock, status) VALUES
(1, 1, 299900, 100, 1),
(2, 2, 499900, 50, 1),
(3, 3, 9900, 200, 1),
(4,  4,  459900, 80,  1),
(5,  5,  129900, 150, 1),
(6,  6,  399900, 60,  1),
(7,  7,  699900, 35,  1),
(8,  8,  19900,  300, 1),
(9,  9,  12900,  500, 1),
(10, 10, 25900,  120, 1),
(11, 11, 15900,  180, 1),
(12, 12, 18900,  160, 1),
(13, 13, 25900,  90,  1),
(14, 14, 21900,  110, 1),
(15, 15, 29900,  200, 1),
(16, 16, 35900,  80,  1),
(17, 17, 8900,   400, 1),
(18, 18, 4900,   600, 1),
(19, 19, 6900,   350, 1),
(20, 20, 5900,   280, 1),
(21, 21, 15900,  150, 1),
(22, 22, 7900,   420, 1),
(23, 23, 3900,   500, 1),
(24, 24, 4900,   380, 1);

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
