# 衣览无余 · AiWear（AI 服装搭配应用）

基于 LangChain 的 AI 服装搭配全栈应用：用户上传服装图 → CLIP 向量化入库 → 支持文搜图 / 图搜图 → 大模型完成图片编辑与双图合成 → 历史记录沉淀。

> 本仓库为**脱敏源码**（所有云凭证已改为环境变量注入，不含任何真实密钥）。
> 配套测试报告见仓库组内的 `aiwear-test-report`。

## 架构

```
Vue3 前端 (Vite :5173)
      │ /api（Vite 代理）
      ▼
Spring Boot 后端 (:8081)  ──转发──▶  Flask + LangChain AI 服务 (:5000)
  │        │                              │
  │      MySQL (:3307)                  CLIP 向量检索
  │   (元数据 / 历史)                 DashScope 通义 / 万相图像模型
  └── Redis (:6379)
   (验证码 / JWT)                    阿里云 OSS（图片存储）
```

## 目录结构

```
aiwear-source/
├── backend/     Spring Boot 3.2.4 + MyBatis-Plus + JWT + OSS（Java 17）
├── python/      Flask + LangChain + CLIP + DashScope（AI 能力）
├── frontend/    Vue3 + Vite + Element Plus + Pinia
├── docker-compose.yml   一键拉起 MySQL + Redis
└── README.md
```

## 技术栈

| 层 | 技术 |
|---|---|
| 后端 | Spring Boot 3.2.4, MyBatis-Plus 3.5.7, JJWT 0.11.5, 阿里云 OSS SDK 3.17.4, BCrypt |
| AI 服务 | Flask, LangChain(ChatTongyi), CLIP(vit-base-patch16), DashScope 图像模型, Redis 向量存储 |
| 前端 | Vue 3, Vite 7, Element Plus, Pinia, vue-router, axios |
| 数据 | MySQL 8（元数据/历史）, Redis 7（验证码/JWT）, OSS（图片） |

## 核心接口

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | /api/user/auth | 登录/注册（用户名密码 或 邮箱验证码），返回 JWT |
| POST | /api/user/send-code | 发送邮箱验证码 |
| POST | /api/user/logout | 登出 |
| POST | /api/file/upload/image | 上传图片到 OSS 并向量化入库 |
| GET | /api/file/my-images | 我的图片列表 |
| POST | /api/file/search | 文搜图（query）/ 图搜图（file） |
| POST | /api/file/edit | 单图 AI 编辑 |
| POST | /api/file/merge | 双图 AI 合成 |
| GET | /api/record/my | 历史记录 |

## 快速开始

### 1) 拉起依赖中间件

```bash
docker compose up -d        # MySQL->3307, Redis->6379，自动导入 backend/init.sql
```

### 2) 配置环境变量

后端（Spring Boot）关键变量，本地用 docker-compose 默认值即可跑通：

| 变量 | 说明 | 本地默认 |
|---|---|---|
| `MYSQL_URL` / `MYSQL_USERNAME` / `MYSQL_PASSWORD` | 数据源 | `...3307/bite_wear` / `bite` / `bite@123` |
| `REDIS_HOST` / `REDIS_PORT` | Redis | `127.0.0.1` / `6379` |
| `MAIL_USERNAME` / `MAIL_PASSWORD` | QQ 邮箱账号 + **授权码** | 需自备（发邮件验证码用） |
| `JWT_SECRET` | JWT 签名密钥 | 生产务必替换为强随机串 |
| `OSS_ENDPOINT` / `OSS_ACCESS_KEY_ID` / `OSS_ACCESS_KEY_SECRET` / `OSS_BUCKET` | 阿里云 OSS | 需自备 |
| `PYTHON_BASE_URL` | AI 服务地址 | `http://127.0.0.1:5000` |

Python AI 服务：复制 `python/.env.example` 为 `python/.env`，填入 `DASHSCOPE_API_KEY`。

前端：`.env.development` 中 `PROXY_TARGET` 指向后端 `http://127.0.0.1:8081`。

### 3) 启动三个服务

```bash
# 后端（Java 17）
cd backend && mvn spring-boot:run

# AI 服务（Python）
cd python && python -m venv .venv && . .venv/bin/activate   # Windows: .venv\Scripts\activate
pip install -r requirements.txt
python download.py            # 下载 CLIP 模型到本地
python server.py

# 前端
cd frontend && npm install && npm run dev
```

访问 http://localhost:5173 。

## 安全说明

- 本仓库**不包含任何真实密钥**；`application.yml`、`server.py` 中的敏感项全部走 `${ENV}` / `os.getenv` 注入，默认值仅用于本地开发。
- `.env` 等真实配置文件已被 `.gitignore` 排除。
- AI 服务默认**关闭 Flask debug**、监听 `127.0.0.1`，避免 Werkzeug 调试器暴露。

## License

仅用于个人作品展示与求职演示。
