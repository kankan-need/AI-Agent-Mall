-- Agent 用户购物偏好
USE learn_mall;

CREATE TABLE IF NOT EXISTS user_agent_preference (
    user_id      BIGINT       NOT NULL COMMENT '用户ID',
    focus_type   VARCHAR(20)  NOT NULL DEFAULT 'BALANCED' COMMENT 'VALUE/QUALITY/BALANCED',
    budget_min   BIGINT       NULL COMMENT '预算下限(分)',
    budget_max   BIGINT       NULL COMMENT '预算上限(分)',
    tags         JSON         NULL COMMENT '偏好标签',
    summary      VARCHAR(500) NULL COMMENT '偏好摘要',
    create_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Agent购物偏好';
