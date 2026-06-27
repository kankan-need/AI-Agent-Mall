# Learn Mall 学习版

参照 [Mall4cloud](https://github.com/gz-yami/mall4cloud) 微服务架构的精简学习项目。

## 当前进度

| Phase | 状态 | 内容 |
|-------|------|------|
| 0 | 完成 | Gateway、Nacos、统一响应、learn-demo |
| 1 | 完成 | auth、rbac、common-security、admin 动态路由 |
| 2 | 完成 | learn-product 商品/分类/购物车、admin 商品管理、H5 浏览 |
| 3 | 完成 | learn-user 注册/资料/地址、H5 个人中心 |
| 4 | 待做 | 订单与支付 |

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
```

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
mvn -pl learn-gateway spring-boot:run
```

### 4. 启动 H5

```powershell
cd front-end/mall-h5
npm install
npm run dev
```

访问 http://localhost:9528

## Phase 3 验证

### 注册

1. H5「我的」→「注册新账号」
2. 填写用户名/密码，注册成功后自动登录

### 个人中心

1. 查看头像昵称
2. 编辑资料
3. 收货地址列表 / 新增 / 编辑 / 删除

### curl 示例

```powershell
# 注册（免登录）
curl -X POST http://127.0.0.1:8000/learn-user/ua/user/register ^
  -H "Content-Type: application/json" ^
  -d "{\"userName\":\"test01\",\"password\":\"123456\",\"nickName\":\"测试用户\"}"

# 地址列表（需 Token）
curl http://127.0.0.1:8000/learn-user/a/user_addr/list -H "Authorization: TOKEN"
```

## 架构说明（Phase 3）

```
H5 → Gateway → learn-user（注册/资料/地址）
              → learn-auth（登录、AccountFeign 写 auth_account）
              → learn-product（商品/购物车）

learn-user
  /ua/user/register   注册并返回 Token
  /a/user/**          用户资料（需登录）
  /a/user_addr/**     收货地址 CRUD（需登录）
```

注册流程：`learn-user` 调 `AccountFeignClient.save` 写 `auth_account` → 插 `user` 表 → `storeTokenAndGetVo` 返回 Token。

## 环境变量

| 变量 | 默认值 |
|------|--------|
| `MYSQL_PORT` | `3306` |
| `MYSQL_PASSWORD` | `123123` |
| `NACOS_HOST` | `127.0.0.1` |

> Nacos 注册 IP 问题见 `learn-common-nacos`；本地开发前端走 Vite 代理，`.env.development` 中 `VITE_APP_BASE_API` 留空。
