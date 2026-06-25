# Learn Mall 学习版

参照 [Mall4cloud](https://github.com/gz-yami/mall4cloud) 微服务架构的精简学习项目。

## 当前进度

| Phase | 状态 | 内容 |
|-------|------|------|
| 0 | 完成 | Gateway、Nacos、统一响应、learn-demo |
| 1 | 完成 | auth、rbac、common-security、admin 动态路由 |
| 2 | 待做 | 商品与购物车 |
| 3 | 待做 | 用户与地址 |
| 4 | 待做 | 订单与支付 |

## 目录结构

```
mall4cloud-learn/
├── learn-common/          # 公共模块
│   ├── learn-common-core/
│   ├── learn-common-cache/
│   ├── learn-common-database/
│   └── learn-common-security/
├── learn-api/             # Feign 契约
│   ├── learn-api-auth/
│   └── learn-api-rbac/
├── learn-gateway/         # 网关 :8000
├── learn-auth/            # 认证 :9101
├── learn-rbac/            # 权限菜单 :9102
├── learn-demo/            # 示例服务 :9100
├── front-end/admin/       # 管理端 Vue3
├── db/                    # SQL 脚本
└── deploy/                # Docker 中间件
```

## 环境要求

- JDK 17、Maven 3.6+
- Docker Desktop（MySQL + Redis + Nacos）
- Node.js 18+（管理端前端）

## 快速启动

### 1. 中间件

```powershell
cd deploy
docker compose up -d
```

| 中间件 | 地址 | 凭证 |
|--------|------|------|
| MySQL | `127.0.0.1:3307` | `root` / `learn123` |
| Redis | `127.0.0.1:6379` | 密码 `learn123` |
| Nacos | `127.0.0.1:8848` | 无鉴权 |

### 2. 初始化数据库

连接 MySQL 后执行：

```
db/phase1-auth-rbac.sql
```

默认管理员：**admin / 123456**（平台端 `sysType=2`）

### 3. 启动后端

```powershell
cd mall4cloud-learn
mvn clean package -DskipTests

# 分别启动（IDEA 或多终端）
mvn -pl learn-auth spring-boot:run
mvn -pl learn-rbac spring-boot:run
mvn -pl learn-gateway spring-boot:run
mvn -pl learn-demo spring-boot:run
```

Nacos 控制台应看到 4 个服务：`learn-auth`、`learn-rbac`、`learn-gateway`、`learn-demo`

### 4. 启动管理端

```powershell
cd front-end/admin
npm install
npm run dev
```

访问 http://localhost:9527 ，使用 admin / 123456 登录。

## Phase 1 验证

1. 登录成功并进入首页
2. 侧边栏显示「控制台」「服务探活」菜单（来自 `/learn-rbac/menu/route` 动态路由）
3. 「服务探活」页点击按钮，经网关调用 `/learn-demo/ping` 返回当前用户名

```powershell
# 登录
curl -X POST http://127.0.0.1:8000/learn-auth/ua/login ^
  -H "Content-Type: application/json" ^
  -d "{\"principal\":\"admin\",\"credentials\":\"123456\",\"sysType\":2}"

# 带 Token 访问受保护接口（将 TOKEN 替换为返回的 accessToken）
curl http://127.0.0.1:8000/learn-demo/ping -H "Authorization: TOKEN"
```

## 架构说明（Phase 1）

```
Admin → Gateway → learn-auth（登录/Token）
                → learn-rbac（菜单/权限）
                → learn-demo（受 AuthFilter 保护）

AuthFilter → TokenFeignClient → learn-auth（Redis 校验 Token）
          → PermissionFeignClient → learn-rbac（URI 权限）
```

## 环境变量

| 变量 | 默认值 |
|------|--------|
| `NACOS_HOST` | `127.0.0.1` |
| `MYSQL_PORT` | `3307` |
| `MYSQL_PASSWORD` | `learn123` |
| `REDIS_PASSWORD` | `learn123` |
