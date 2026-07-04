USE learn_mall;

-- ========== 优惠券表 ==========
DROP TABLE IF EXISTS user_coupon;
DROP TABLE IF EXISTS coupon;

CREATE TABLE coupon (
    coupon_id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    name        VARCHAR(100)    NOT NULL COMMENT '优惠券名称',
    amount      BIGINT          NOT NULL COMMENT '优惠金额(分)',
    min_amount  BIGINT          NOT NULL COMMENT '最低消费金额(分)',
    valid_days  INT             NOT NULL DEFAULT 7 COMMENT '有效天数',
    status      TINYINT         NOT NULL DEFAULT 1 COMMENT '0=禁用 1=启用',
    PRIMARY KEY (coupon_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券';

CREATE TABLE user_coupon (
    user_coupon_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    create_time    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id        BIGINT          NOT NULL COMMENT '用户ID',
    coupon_id      BIGINT UNSIGNED NOT NULL COMMENT '优惠券ID',
    claim_time     DATETIME        NOT NULL COMMENT '领取时间',
    expire_time    DATETIME        NOT NULL COMMENT '过期时间',
    status         VARCHAR(20)     NOT NULL DEFAULT 'unused' COMMENT 'unused/used',
    PRIMARY KEY (user_coupon_id),
    KEY idx_user_id (user_id),
    KEY idx_coupon_id (coupon_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券';

-- 初始数据（与前端硬编码的4张券保持一致）
INSERT INTO coupon (coupon_id, name, amount, min_amount, valid_days, status) VALUES
(1, '新人专享券', 1000, 5000, 7, 1),
(2, '满减优惠券', 2000, 10000, 15, 1),
(3, '数码家电券', 5000, 30000, 30, 1),
(4, '服饰专享券', 1500, 8000, 15, 1);

-- ========== 管理端菜单 ==========
INSERT INTO menu (menu_id, parent_id, biz_type, path, component, redirect, hidden, name, title, icon, seq) VALUES
(12, 1, 2, 'marketing', 'Layout', '/marketing/coupon/index', 0, 'Marketing', '营销管理', 'present', 5),
(13, 12, 2, 'coupon', 'marketing/coupon/index', NULL, 0, 'MarketingCoupon', '优惠券管理', 'ticket', 1);

INSERT INTO role_menu (role_id, menu_id) VALUES
(1, 12), (1, 13);
