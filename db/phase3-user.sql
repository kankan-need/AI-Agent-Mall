USE learn_mall;

DROP TABLE IF EXISTS user_addr;
DROP TABLE IF EXISTS user;

CREATE TABLE user (
    user_id     BIGINT       NOT NULL COMMENT '与 auth_account.user_id 对齐',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    nick_name   VARCHAR(255) DEFAULT NULL,
    pic         VARCHAR(255) DEFAULT NULL,
    status      INT          NOT NULL DEFAULT 1 COMMENT '1正常 0无效',
    PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='C端用户资料';

CREATE TABLE user_addr (
    addr_id     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id     BIGINT          NOT NULL,
    mobile      VARCHAR(20)     DEFAULT NULL,
    is_default  TINYINT         NOT NULL DEFAULT 0 COMMENT '1默认',
    consignee   VARCHAR(50)     DEFAULT NULL,
    province_id BIGINT          DEFAULT NULL,
    province    VARCHAR(100)    DEFAULT NULL,
    city_id     BIGINT          DEFAULT NULL,
    city        VARCHAR(20)     DEFAULT NULL,
    area_id     BIGINT          DEFAULT NULL,
    area        VARCHAR(20)     DEFAULT NULL,
    post_code   VARCHAR(15)     DEFAULT NULL,
    addr        VARCHAR(255)    DEFAULT NULL,
    PRIMARY KEY (addr_id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收货地址';

-- 为 phase2 测试账号 user1 补用户资料
INSERT INTO user (user_id, nick_name, pic, status) VALUES
(2, '测试用户', 'https://img.yzcdn.cn/vant/cat.jpeg', 1)
ON DUPLICATE KEY UPDATE nick_name = VALUES(nick_name);

-- 示例地址
INSERT INTO user_addr (user_id, mobile, is_default, consignee, province, city, area, addr) VALUES
(2, '13800138000', 1, '张三', '广东省', '广州市', '天河区', '体育西路 1 号');
