# Learn Mall 学习版

参照 [Mall4cloud](https://github.com/gz-yami/mall4cloud) 微服务架构的精简学习项目。

## 当前进度

| Phase | 状态 | 内容 |
|-------|------|------|
| 0 | 完成 | Gateway、Nacos、统一响应、learn-demo |
| 1 | 完成 | auth、rbac、common-security、admin 动态路由 |
| 2 | 完成 | learn-product 商品/分类/购物车、admin 商品管理、H5 浏览 |
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
├── learn-product/         # 商品 :9114
├── learn-demo/            # 示例服务 :9100
├── front-end/admin/       # 管理端 Vue3
├── front-end/mall-h5/     # C端 H5 Vue3
├── db/                    # SQL 脚本
└── deploy/                # Docker 中间件
```

## 环境要求

- JDK 17、Maven 3.6+
- Docker Desktop（MySQL + Redis + Nacos）
- Node.js 18+（管理端 / H5 前端）

## 快速启动

### 1. 中间件

```powershell
cd deploy
docker compose up -d
```

| 中间件 | 地址 | 凭证 |
|--------|------|------|
| MySQL | `127.0.0.1:3306` | `root` / `123123` |
| Redis | `127.0.0.1:6379` | 无密码 |
| Nacos | `127.0.0.1:8848` | 无鉴权 |

### 2. 初始化数据库

连接 MySQL 后依次执行：

```
db/phase1-auth-rbac.sql
db/phase2-product.sql
```

| 账号 | 密码 | 用途 |
|------|------|------|
| admin | 123456 | 管理端（sysType=2） |
| user1 | 123456 | H5 购物车（sysType=0） |

### 3. 启动后端

```powershell
cd mall4cloud-learn
mvn clean package -DskipTests

mvn -pl learn-auth spring-boot:run
mvn -pl learn-rbac spring-boot:run
mvn -pl learn-product spring-boot:run
mvn -pl learn-gateway spring-boot:run
```

Nacos 应看到：`learn-auth`、`learn-rbac`、`learn-product`、`learn-gateway`

### 4. 启动管理端

```powershell
cd front-end/admin
npm install
npm run dev
```

访问 http://localhost:9527 ，使用 **admin / 123456** 登录，侧边栏可见「商品管理」。

### 5. 启动 H5

```powershell
cd front-end/mall-h5
npm install
npm run dev
```

访问 http://localhost:9528 ，可匿名浏览商品；加购需用 **user1 / 123456** 登录。

## Phase 2 验证

### 管理端

1. 「分类管理」查看/新增/编辑分类
2. 「商品列表」分页、上下架、编辑
3. 新增商品后 H5 首页可见

### H5

1. 首页按分类筛选商品
2. 商品详情页查看信息与详情 HTML
3. 登录后加购、购物车增减/勾选/删除

### curl 示例

```powershell
# C端商品列表（免登录）
curl "http://127.0.0.1:8000/learn-product/ua/spu/page?pageNum=1&pageSize=10"

# C端登录
curl -X POST http://127.0.0.1:8000/learn-auth/ua/login ^
  -H "Content-Type: application/json" ^
  -d "{\"principal\":\"user1\",\"credentials\":\"123456\",\"sysType\":0}"

# 加购（将 TOKEN 替换为 accessToken）
curl -X POST http://127.0.0.1:8000/learn-product/a/shop_cart/change_item ^
  -H "Authorization: TOKEN" -H "Content-Type: application/json" ^
  -d "{\"spuId\":1,\"skuId\":1,\"count\":1}"
```

## 架构说明（Phase 2）

```
Admin/H5 → Gateway → learn-product（分类/SPU/SKU/购物车）
                   → learn-auth（登录 Token）
                   → learn-rbac（管理端菜单）

learn-product
  /ua/**  匿名浏览（分类、商品列表、详情）
  /a/**   需登录（购物车）
  /admin/** 需登录 + RBAC（商品管理）
```

## 环境变量

| 变量 | 默认值 |
|------|--------|
| `NACOS_HOST` | `127.0.0.1` |
| `MYSQL_PORT` | `3306` |
| `MYSQL_PASSWORD` | `123123` |
| `REDIS_HOST` | `127.0.0.1` |
| `REDIS_PORT` | `6379` |
