# learn-mall 学习版

参照 [Mall4cloud](https://github.com/gz-yami/mall4cloud) 微服务架构的精简学习项目。

## Phase 0 目标

- Nacos 服务注册与发现
- Gateway 统一入口与动态路由
- 统一响应体 `ServerResponseEntity`
- 中间件三件套：MySQL + Redis + Nacos

## 目录结构

```
mall4cloud-learn/
├── learn-common/
│   └── learn-common-core/     # 统一响应、异常处理
├── learn-gateway/             # API 网关 :8000
├── learn-demo/                # 示例服务 :9100
└── deploy/
    ├── docker-compose.yml     # 中间件
    ├── mysql/initdb/          # 数据库初始化
    └── nacos-config/          # 后续阶段 Nacos 配置模板
```

## 环境要求

- JDK 17
- Maven 3.6+
- Docker Desktop（Windows 可用）

## 快速启动

### 1. 启动中间件

```powershell
cd deploy
docker compose up -d
```

等待 Nacos 就绪后访问：http://127.0.0.1:8080/index.html

| 中间件 | 地址 | 账号 |
|--------|------|------|
| MySQL | `127.0.0.1:3307` | `root` / `123123` |
| Redis | `127.0.0.1:6379` | 无密码 |
| Nacos | `127.0.0.1:8848` | 学习版已关闭鉴权 |

> MySQL 映射到宿主机 **3307** 端口，避免与本机已有 MySQL 冲突。

### 2. 编译项目

```powershell
cd mall4cloud-learn
mvn clean package -DskipTests
```

### 3. 启动微服务

在 IDEA 中分别运行，或命令行：

```powershell
# 终端 1：示例服务
mvn -pl learn-demo spring-boot:run

# 终端 2：网关
mvn -pl learn-gateway spring-boot:run
```

### 4. 验证

**直连示例服务：**

```powershell
curl http://127.0.0.1:9100/ping
```

期望响应：

```json
{"code":"00000","msg":"ok","data":{"service":"learn-demo","status":"ok","phase":"0"}}
```

**经网关路由（服务发现自动路由）：**

```powershell
curl http://127.0.0.1:8000/learn-demo/ping
```

**健康检查：**

```powershell
curl http://127.0.0.1:8000/actuator/health
curl http://127.0.0.1:9100/actuator/health
```

**Nacos 控制台：** 应看到 `learn-gateway` 和 `learn-demo` 两个实例。

## 环境变量

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `NACOS_HOST` | `127.0.0.1` | Nacos 地址 |
| `NACOS_PORT` | `8848` | Nacos 端口 |
| `NACOS_USERNAME` | `nacos` | Nacos 用户名（鉴权关闭时可忽略） |
| `NACOS_PASSWORD` | `nacos` | Nacos 密码 |

## 后续阶段

| Phase | 内容 |
|-------|------|
| 1 | auth + rbac + common-security |
| 2 | product 商品与购物车 |
| 3 | user 用户与地址 |
| 4 | order + payment 下单支付 |

## 参照说明

本项目参照 Mall4cloud 的以下模式，但刻意精简：

- 保留：`ServerResponseEntity`、Gateway + Nacos 发现、多模块 Maven 结构
- 暂不引入：Seata、RocketMQ、Elasticsearch、Canal、MinIO、Leaf
