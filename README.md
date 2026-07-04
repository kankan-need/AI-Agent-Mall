# Learn Mall 学习版

参照 [Mall4cloud](https://github.com/gz-yami/mall4cloud) 微服务架构的精简学习项目。

## 当前进度

| Phase | 状态 | 内容 |
|-------|------|------|
| 0 | 完成 | Gateway、Nacos、统一响应、learn-demo |
| 1 | 完成 | auth、rbac、common-security、admin 动态路由 |
| 2 | 完成 | learn-product 商品/分类/购物车、admin 商品管理、H5 浏览 |
| 3 | 完成 | learn-user 注册/资料/地址、H5 个人中心 |
| 4 | 完成 | learn-order 下单/锁库存/模拟支付/定时取消、双端订单页 |
| 5 | 完成 | learn-agent Spring AI 购物助手、H5 Agent 对话与偏好管理 |
| 6 | 完成 | 优惠券系统（后台管理+领券）+ 首页红包广告弹窗 |

## 目录结构

```
mall4cloud-learn/
├── learn-common/
├── learn-api/
├── learn-gateway/         # :8000
├── learn-auth/            # :9101
├── learn-rbac/            # :9102
├── learn-product/         # :9114
├── learn-user/            # :9115
├── learn-order/           # :9116
├── learn-agent/           # :9117
├── learn-demo/            # :9100
├── front-end/admin/
├── front-end/mall-h5/
├── db/
└── deploy/
```

## 快速启动

### 1. 中间件

```powershell
cd deploy
docker compose up -d
```

### 2. 初始化数据库

依次执行：

```
db/phase1-auth-rbac.sql
db/phase2-product.sql
db/phase3-user.sql
db/phase4-order.sql
db/phase5-coupon.sql
db/phase5-agent.sql
```

> 已有数据库只需补充商品数据时，可额外执行 `db/product-more-data.sql`（增量插入，不会清空表）。

### Agent 配置（Phase 5）

启动 `learn-agent` 前需配置 DeepSeek API Key：

```powershell
$env:DEEPSEEK_API_KEY="你的DeepSeek密钥"
mvn -pl learn-agent spring-boot:run
```

H5 底部 **Agent** 入口进入对话；**我的 → 购物偏好** 可查看/修改偏好。

### 商品数据概览

| 项目 | 数量 |
|------|------|
| 一级分类 | 4（数码家电、服饰鞋包、食品饮料、家居生活） |
| 二级分类 | 10 |
| 上架商品 | 24 |

价格区间约 ¥39 ~ ¥6999，覆盖手机、电脑、服饰、零食、家居等品类。

| 账号 | 密码 | 用途 |
|------|------|------|
| admin | 123456 | 管理端（sysType=2） |
| user1 | 123456 | H5 测试（sysType=0，含示例地址） |

### 3. 启动后端

```powershell
cd mall4cloud-learn
mvn clean package -DskipTests

mvn -pl learn-auth spring-boot:run
mvn -pl learn-rbac spring-boot:run
mvn -pl learn-product spring-boot:run
mvn -pl learn-user spring-boot:run
mvn -pl learn-order spring-boot:run
mvn -pl learn-agent spring-boot:run
mvn -pl learn-gateway spring-boot:run
```

### 4. 启动前端

**H5**

```powershell
cd front-end/mall-h5
npm install
npm run dev
```

访问 http://localhost:9528

**Admin**

```powershell
cd front-end/admin
npm install
npm run dev
```

访问 http://localhost:9527

## Phase 4 验证

### H5 下单流程

1. 使用 `user1 / 123456` 登录
2. 首页加购商品 → 购物车勾选 →「去结算」
3. 选择收货地址 → 提交订单
4. 支付页点击「模拟支付」→ 订单变为已付款
5. 「我的」→「我的订单」查看列表/详情
6. 待付款订单可取消；超时未付由定时任务自动关闭（默认 30 分钟）

### Admin 订单管理

1. 使用 `admin / 123456` 登录
2. 左侧菜单「订单管理」→ 订单列表
3. 按状态筛选，点击「详情」查看订单快照

### curl 示例

```powershell
# 提交订单（需 Token，且购物车有勾选商品）
curl -X POST http://127.0.0.1:8000/learn-order/a/order/submit ^
  -H "Content-Type: application/json" ^
  -H "Authorization: TOKEN" ^
  -d "{\"addrId\":1}"

# 模拟支付
curl -X POST "http://127.0.0.1:8000/learn-order/a/order/pay?orderId=1" ^
  -H "Authorization: TOKEN"

# 管理端订单分页
curl "http://127.0.0.1:8000/learn-order/admin/order/page?pageNum=1&pageSize=10" ^
  -H "Authorization: ADMIN_TOKEN"
```

## 架构说明（Phase 4）

```
H5 → Gateway → learn-order（下单/支付/取消）
              → learn-product（购物车、锁库存 Feign）
              → learn-user（收货地址 Feign）

learn-order
  /a/order/submit      下单（锁库存 → 落单 → 删购物车）
  /a/order/pay         模拟支付（写 pay_info、确认库存）
  /a/order/cancel      用户取消（解锁库存）
  /admin/order/**      管理端订单查询

OrderCancelTask        每分钟扫描超时未支付订单并关闭
```

订单状态：`1` 待付款、`2` 已付款、`5` 完成、`6` 关闭。

## Phase 3 验证（用户模块）

### 注册

1. H5「我的」→「注册新账号」
2. 填写用户名/密码，注册成功后自动登录

### 个人中心

1. 查看头像昵称
2. 编辑资料
3. 收货地址列表 / 新增 / 编辑 / 删除

## 环境变量

| 变量 | 默认值 |
|------|--------|
| `MYSQL_PORT` | `3306` |
| `MYSQL_PASSWORD` | `123123` |
| `NACOS_HOST` | `127.0.0.1` |

> Nacos 注册 IP 问题见 `learn-common-nacos`；本地开发前端走 Vite 代理，`.env.development` 中 `VITE_APP_BASE_API` 留空。

## Phase 6 优惠券 + 广告弹窗

### 数据库

`coupon` 表（优惠券模板）和 `user_coupon` 表（用户领取记录），menu 表新增 `营销管理 > 优惠券管理`。

### 后端（learn-product 模块）

| 接口 | 路径 | 说明 |
|------|------|------|
| 公开 | `GET /learn-product/ua/coupon/list` | 查看可用优惠券 |
| 需登录 | `POST /learn-product/a/coupon/claim?couponId=1` | 领取优惠券 |
| 需登录 | `GET /learn-product/a/coupon/my` | 我的优惠券 |
| 管理端 | `GET/POST/PUT/DELETE /learn-product/admin/coupon` | 优惠券 CRUD |

关键类：

```
learn-product/src/main/java/com/learn/mall/product/
├── model/Coupon.java, UserCoupon.java
├── mapper/CouponMapper.java/xml, UserCouponMapper.java/xml
├── service/CouponService.java, impl/CouponServiceImpl.java
└── controller/admin/AdminCouponController.java
             app/AppCouponController.java
```

用户身份通过 `AuthUserContext.get().getUserId()` 获取（`/a/**` 路径走 AuthFilter）。

### Admin 管理端

- 菜单「营销管理 > 优惠券管理」→ 表格展示所有券 → 新增/编辑/删除
- 编辑时可修改优惠金额、最低消费、有效天数、启用状态
- 金额输入用**元**，保存时转为**分**存储

### H5 首页广告弹窗

- 首次打开首页弹出红包广告窗（仿 mall4cloud 锯齿券样式）
- 显示 3 张优惠券：红色/金色渐变左栏 + 半圆锯齿切口 + 虚线分隔
- localStorage `learn_mall_ad_seen` 控制只弹一次
- 数据优先从 `/ua/coupon/list` 拉取，失败时使用兜底数据

### H5 领券中心

- 改为走后端 API（不再用 localStorage 硬编码）
- 领券中心：查看可用券 → 点击「立即领取」→ 写入 `user_coupon` 表
- 我的优惠券：查看已领券，含领取时间、过期时间、使用状态

### 设计要点

- 金额统一用**分**存储（Long），前端 `formatPrice()` 除以 100 显示元
- 优惠券过期时间 = 领取时间 + `valid_days`
- Redis 缓存重启后需清理，否则菜单可能显示旧数据
