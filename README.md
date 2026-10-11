# 木羽 AI 超级智能体（xu-ai-agent）

基于 **Spring Boot 3 + Spring AI + Spring AI Alibaba** 构建的 AI 智能体应用平台，包含两个核心 AI 应用、一套自主规划智能体框架、丰富的工具集，以及一个可独立部署的 MCP 服务端。

- **AI 超级智能体（XuManus）**：基于 ReAct（思考-行动）模式自主规划、多步调用工具完成复杂任务的通用智能体
- **AI 恋爱大师（LoveApp）**：支持多轮对话记忆、RAG 知识库问答、结构化输出、工具与 MCP 调用的垂直领域应用
- **工具集**：文件读写、联网搜索、网页抓取、资源下载、终端执行、PDF 生成、任务终止
- **MCP**：既作为 MCP 客户端接入外部服务（高德地图、图片搜索），也提供自研 MCP 服务端（xu-image-search-mcp-server）
- **前端**：Vue 3 + Vite 单页应用，SSE 流式对话，赛博朋克风格界面

---

## 目录

- [项目结构](#项目结构)
- [技术栈](#技术栈)
- [核心功能](#核心功能)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [API 接口](#api-接口)
- [核心架构解析](#核心架构解析)
- [安全与注意事项](#安全与注意事项)

---

## 项目结构

```
xu-ai-agent
├── src/main/java/com/muyu/xuaiagent
│   ├── XuAiAgentApplication.java        # 主启动类（排除 PgVector 自动配置）
│   ├── agent/                           # 自主规划智能体框架
│   │   ├── BaseAgent.java               # 抽象基类：状态管理、步骤循环、SSE 流式输出
│   │   ├── ReActAgent.java              # ReAct 模式：think() + act()
│   │   ├── ToolCallAgent.java           # 工具调用代理：解析并执行工具调用
│   │   ├── XuManus.java                 # AI 超级智能体实现（maxSteps = 20）
│   │   └── model/AgentState.java        # 状态枚举：IDLE / RUNNING / FINISHED / ERROR
│   ├── app/LoveApp.java                 # AI 恋爱大师应用
│   ├── advisor/                         # 自定义 Advisor
│   │   ├── MyLoggerAdvisor.java         # 请求/响应日志
│   │   └── ReReadingAdvisor.java        # Re2 推理增强（重复阅读问题）
│   ├── chatmemory/FileBasedChatMemory.java  # 基于 Kryo 的文件持久化对话记忆
│   ├── config/CorsConfig.java           # 全局跨域配置
│   ├── constant/FileConstant.java       # 文件保存根目录（tmp/）
│   ├── controller/
│   │   ├── AiController.java            # AI 对话接口（同步 / SSE / SseEmitter）
│   │   └── HealthController.java        # 健康检查
│   ├── demo/                            # 学习/示例代码
│   │   ├── invoke/                      # 4 种大模型调用方式（HTTP / SDK / Spring AI / LangChain4j）
│   │   └── rag/MultiQueryExpanderDemo.java  # 多查询扩展示例
│   ├── rag/                             # RAG 检索增强体系
│   │   ├── LoveAppVectorStoreConfig.java        # 内存向量库（SimpleVectorStore）
│   │   ├── PgVectorVectorStoreConfig.java       # PostgreSQL + pgvector 向量库
│   │   ├── LoveAppRagCloudAdvisorConfig.java    # 阿里云百炼云知识库
│   │   ├── LoveAppDocumentLoader.java           # Markdown 文档加载器
│   │   ├── LoveAppRagCustomAdvisorFactory.java  # 自定义 RAG Advisor（元信息过滤）
│   │   ├── LoveAppContextualQueryAugmenterFactory.java  # 空上下文兜底
│   │   ├── MyKeywordEnricher.java               # AI 关键词元信息增强
│   │   ├── MyTokenTextSplitter.java             # 自定义 Token 切分器
│   │   └── QueryRewriter.java                   # 查询重写器
│   └── tools/                           # 工具集
│       ├── ToolRegistration.java        # 集中注册所有工具
│       ├── FileOperationTool.java       # 文件读写
│       ├── WebSearchTool.java           # 联网搜索（SearchAPI.io / 百度）
│       ├── WebScrapingTool.java         # 网页抓取（jsoup）
│       ├── ResourceDownloadTool.java    # 资源下载
│       ├── TerminalOperationTool.java   # 终端命令执行
│       ├── PDFGenerationTool.java       # PDF 生成（iText）
│       └── TerminateTool.java           # 任务终止工具
├── src/main/resources
│   ├── application.yml                  # 主配置（端口、MCP、Knife4j 等）
│   ├── application-local.yml            # 本地配置（API Key、数据源）※ 已被 git 忽略
│   ├── application-prod.yml             # 生产配置 ※ 已被 git 忽略
│   ├── document/                        # 恋爱知识库文档（单身篇 / 恋爱篇 / 已婚篇）
│   └── mcp-servers.json                 # stdio 模式 MCP 服务列表 ※ 已被 git 忽略
├── xu-ai-agent-frontend/                # 前端工程（Vue 3 + Vite）
│   ├── src/
│   │   ├── api/index.js                 # Axios + SSE 封装
│   │   ├── router/index.js              # 路由（首页 / 恋爱大师 / 超级智能体）
│   │   ├── components/ChatRoom.vue      # 通用聊天室组件
│   │   └── views/                       # Home / LoveMaster / SuperAgent
│   ├── Dockerfile                       # 多阶段构建（Node 构建 + Nginx 托管）
│   └── nginx.conf                       # 静态资源 + /api 反向代理（含 SSE 配置）
├── xu-image-search-mcp-server/          # 图片搜索 MCP 服务端（独立 Spring Boot 工程）
│   └── src/main/java/.../tools/ImageSearchTool.java   # Pexels 图片搜索工具
├── Dockerfile                           # 后端镜像构建
└── tmp/                                 # 工具运行产物（文件/下载/PDF/对话记忆）
```

---

## 技术栈

| 分类 | 技术 | 版本 |
| --- | --- | --- |
| 语言 / 运行时 | Java | 21 |
| 基础框架 | Spring Boot | 3.4.10 |
| AI 框架 | Spring AI | 1.0.0-M6 |
| AI 框架 | Spring AI Alibaba（DashScope） | 1.0.0-M6.1 |
| 大模型 | 阿里云百炼 `qwen3-max` | - |
| LLM 调用 | LangChain4j（DashScope） | 1.0.0-beta2 |
| 向量库 | PostgreSQL + pgvector / SimpleVectorStore | - |
| MCP | Spring AI MCP Client / Server（SSE、stdio） | 1.0.0-M6 |
| 文档解析 | Markdown Document Reader、jsoup | 1.19.1 |
| PDF | iText | 9.1.0 |
| 序列化 | Kryo（对话记忆持久化） | 5.6.2 |
| 工具库 | Hutool | 5.8.37 |
| API 文档 | Knife4j（OpenAPI 3） | 4.4.0 |
| 前端 | Vue 3 + Vite + vue-router + Axios | 3.2 / 4.x |
| 部署 | Docker（Amazon Corretto 21）+ Nginx | - |

---

## 核心功能

### 1. AI 超级智能体（XuManus）

自主规划智能体，采用经典 **ReAct（Reasoning + Acting）** 循环：

- **状态机管理**：`IDLE → RUNNING → FINISHED / ERROR`，非法状态拒绝执行
- **多步执行**：最多 20 步（`maxSteps`），每步先 `think()` 决策、再 `act()` 执行
- **自主工具调用**：由大模型决定调用哪些工具及参数，`ToolCallingManager` 负责执行并回填上下文
- **自动终止**：模型判断任务完成时调用 `doTerminate` 工具，代理切换到 `FINISHED` 状态
- **流式反馈**：通过 `SseEmitter` 实时推送每一步的执行结果（5 分钟超时）

### 2. AI 恋爱大师（LoveApp）

深耕恋爱心理领域的垂直应用，演示了 Spring AI 的多种核心能力：

| 能力 | 实现 |
| --- | --- |
| 多轮对话记忆 | `MessageChatMemoryAdvisor` + `InMemoryChatMemory`（可选 Kryo 文件持久化） |
| 同步对话 | `doChat()`，返回完整回复 |
| 流式对话 | `doChatByStream()`，返回 `Flux<String>`，支持 SSE / SseEmitter 三种推送方式 |
| 结构化输出 | `doChatWithReport()` 输出 `LoveReport(title, suggestions)` 记录对象 |
| 知识库问答 | `doChatWithRag()` 查询重写 + 向量检索增强 |
| 工具调用 | `doChatWithTools()` 挂载全部本地工具 |
| MCP 调用 | `doChatWithMcp()` 接入 MCP 服务（如高德地图、图片搜索） |
| 推理增强 | `ReReadingAdvisor`（Re2 技巧，可按需开启） |

### 3. RAG 检索增强体系

项目实现了 **四种知识库方案**，可按需切换（`LoveApp.doChatWithRag()` 中调整 Advisor）：

1. **内存向量库（SimpleVectorStore）**：启动时加载 `resources/document/*.md`（单身篇 / 恋爱篇 / 已婚篇），经 `MyKeywordEnricher` 调用 AI 补齐关键词元信息后入库
2. **PgVector 向量库**：PostgreSQL + pgvector，HNSW 索引、余弦距离、1536 维，自动建表
3. **阿里云百炼云知识库**：`DashScopeDocumentRetriever` 检索托管知识库"恋爱大师"
4. **自定义 RAG Advisor**：`VectorStoreDocumentRetriever` + 元信息过滤（`status = 单身 / 恋爱 / 已婚`），相似度阈值 0.5、TopK 3，并配置了空上下文兜底话术

另配套 `QueryRewriter`（查询重写）与 `MultiQueryExpanderDemo`（多查询扩展，每次扩展 3 条）。

### 4. 工具集

所有工具通过 `ToolRegistration` 集中注册为 `ToolCallback[]`，基于 `@Tool` 注解声明：

| 工具 | 功能 | 产物目录 |
| --- | --- | --- |
| `FileOperationTool` | 读/写文本文件 | `tmp/file/` |
| `WebSearchTool` | 调用 SearchAPI.io 百度搜索，取前 5 条结果 | - |
| `WebScrapingTool` | jsoup 抓取网页 HTML | - |
| `ResourceDownloadTool` | 下载 URL 资源到本地 | `tmp/download/` |
| `TerminalOperationTool` | 执行终端命令（Windows `cmd.exe`） | - |
| `PDFGenerationTool` | iText 生成 PDF（内置中文字体 STSongStd-Light） | `tmp/pdf/` |
| `TerminateTool` | 通知智能体合理结束任务 | - |

### 5. MCP（Model Context Protocol）

**作为客户端**（主应用）：

- SSE 模式：连接 `http://localhost:8127`（见 `application.yml` 中 `spring.ai.mcp.client.sse.connections.server1`）
- stdio 模式：通过 `resources/mcp-servers.json` 配置本地进程服务，例如：
  - `amap-maps`：高德地图 MCP 服务（npx 启动）
  - `xu-image-search-mcp-server`：本项目自研的图片搜索 MCP 服务（jar 启动）

**作为服务端**（`xu-image-search-mcp-server`）：

- 提供 `searchImage` 工具，调用 Pexels API 搜索图片
- 支持双模式运行：
  - **SSE 模式**：`application-sse.yml`，监听端口 `8127`
  - **stdio 模式**：`application-stdio.yml`，非 Web 应用，供父进程以标准输入输出通信

### 6. 前端

Vue 3 单页应用，包含三个页面：

| 路由 | 页面 | 说明 |
| --- | --- | --- |
| `/` | Home | 平台首页，卡片入口 |
| `/love-master` | AI 恋爱大师 | 带会话 ID 的情感咨询对话 |
| `/super-agent` | AI 超级智能体 | 任务型对话，展示智能体执行步骤 |

- 通过原生 `EventSource` 消费后端 SSE 流式响应（`src/api/index.js`）
- `ChatRoom.vue` 为通用聊天室组件，支持打字指示器、消息时间、连接状态
- 开发环境直连 `http://localhost:8123/api`；生产环境走 Nginx 同域 `/api` 反向代理

---

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.9+（或使用项目自带 `mvnw`）
- Node.js 18+（前端）
- PostgreSQL + pgvector（可选，仅 PgVector 向量库需要）
- 各平台 API Key（见 [配置说明](#配置说明)）

### 1. 创建本地配置

由于 `application-local.yml`、`application-prod.yml`、`mcp-servers.json` **未纳入版本控制**，克隆后需自行创建：

```yaml
# src/main/resources/application-local.yml
spring:
  ai:
    dashscope:
      api-key: <你的阿里云百炼 API Key>
      chat:
        options:
          model: qwen3-max
  datasource:
    url: jdbc:postgresql://<主机>/xu_ai_agent
    username: <用户名>
    password: <密码>

search-api:
  api-key: <SearchAPI.io API Key>
```

> 仅使用内存向量库时 `datasource` 可省略，但 `PgVectorVectorStoreConfig` 会尝试连接数据库，建议保留或按需注释。

### 2. 启动后端

```bash
# 项目根目录
./mvnw spring-boot:run
# 或
mvn spring-boot:run
```

启动后：

- 服务地址：`http://localhost:8123/api`
- 健康检查：`http://localhost:8123/api/health`
- 接口文档（Knife4j）：`http://localhost:8123/api/doc.html`

> 注意：`demo/invoke/SpringAiAiInvoke` 是一个 `CommandLineRunner`，应用启动时会自动调用一次大模型并打印结果，如不需要可移除 `@Component` 注解。

### 3. 启动前端

```bash
cd xu-ai-agent-frontend
npm install
npm run dev
```

访问 `http://localhost:3001`。

### 4. 启动图片搜索 MCP 服务（可选）

```bash
cd xu-image-search-mcp-server
./mvnw clean package -DskipTests

# SSE 模式（默认，端口 8127，供主应用连接）
java -jar target/xu-image-search-mcp-server-0.0.1-SNAPSHOT.jar --spring.profiles.active=sse

# stdio 模式（供 mcp-servers.json 拉起）
java -Dspring.ai.mcp.server.stdio=true -Dspring.main.web-application-type=none \
     -Dlogging.pattern.console= -jar target/xu-image-search-mcp-server-0.0.1-SNAPSHOT.jar
```

### 5. Docker 部署

**后端**：

```bash
docker build -t xu-ai-agent .
docker run -p 8123:8123 --name xu-ai-agent xu-ai-agent
# 镜像内以 --spring.profiles.active=prod 启动，需保证 prod 配置可用
```

**前端**：

```bash
cd xu-ai-agent-frontend
docker build -t xu-ai-agent-frontend .
docker run -p 80:80 --name xu-ai-agent-frontend xu-ai-agent-frontend
```

前端镜像使用 Nginx 托管静态文件，并将 `/api` 反向代理到后端（含 SSE 免缓冲配置，见 `nginx.conf`）。

---

## 配置说明

主配置 `application.yml`：

| 配置项 | 说明 |
| --- | --- |
| `server.port` | 服务端口，默认 `8123` |
| `server.servlet.context-path` | 统一前缀 `/api` |
| `spring.profiles.active` | 默认 `local` |
| `spring.ai.vectorstore.pgvector.*` | HNSW 索引、1536 维、余弦距离、批量上限 10000 |
| `spring.ai.mcp.client.sse.connections.server1.url` | MCP SSE 服务地址（默认 `http://localhost:8127`） |
| `springdoc` / `knife4j` | Swagger / Knife4j 接口文档配置（中文） |
| `logging.level.org.springframework.ai` | `DEBUG`，便于观察 AI 调用过程 |

环境相关配置（`application-local.yml` / `application-prod.yml`）：

| 配置项 | 说明 |
| --- | --- |
| `spring.ai.dashscope.api-key` | 阿里云百炼 API Key |
| `spring.ai.dashscope.chat.options.model` | 对话模型，默认 `qwen3-max` |
| `spring.datasource.*` | PostgreSQL 数据源（PgVector 向量库使用） |
| `search-api.api-key` | SearchAPI.io 搜索服务 Key（`WebSearchTool` 使用） |

其他密钥位置：

- 图片搜索：`ImageSearchTool.API_KEY`（Pexels API Key）
- 高德地图 MCP：`mcp-servers.json` 中 `AMAP_MAPS_API_KEY`

---

## API 接口

统一前缀：`http://localhost:8123/api`

| 方法 | 路径 | 参数 | 说明 | 返回类型 |
| --- | --- | --- | --- | --- |
| GET | `/health` | - | 健康检查 | `String`（ok） |
| GET | `/ai/love_app/chat/sync` | `message`, `chatId` | 恋爱大师·同步对话 | `String` |
| GET | `/ai/love_app/chat/sse` | `message`, `chatId` | 恋爱大师·SSE 流式对话 | `Flux<String>` |
| GET | `/ai/love_app/chat/server_sent_event` | `message`, `chatId` | 恋爱大师·SSE（ServerSentEvent 包装） | `Flux<ServerSentEvent>` |
| GET | `/ai/love_app/chat/sse_emitter` | `message`, `chatId` | 恋爱大师·SseEmitter 流式（3 分钟超时） | `SseEmitter` |
| GET | `/ai/manus/chat` | `message` | 超级智能体·流式执行（5 分钟超时） | `SseEmitter` |

**调用示例**：

```bash
# 同步对话
curl "http://localhost:8123/api/ai/love_app/chat/sync?message=我喜欢一个同事，怎么开口&chatId=user_001"

# 流式对话（SSE）
curl -N "http://localhost:8123/api/ai/love_app/chat/sse?message=如何拓展社交圈&chatId=user_001"

# 超级智能体（自主调用工具完成任务）
curl -N "http://localhost:8123/api/ai/manus/chat?message=帮我搜索南京的约会地点，并生成一份PDF约会计划"
```

> `chatId` 用于隔离多轮对话记忆，同一会话请保持 `chatId` 不变。

---

## 核心架构解析

### 智能体执行流程

```
             BaseAgent（状态机 + 步骤循环 + SSE 流式）
                  │
                  ▼
             ReActAgent（step = think() → act()）
                  │
                  ▼
             ToolCallAgent（工具解析 / 执行 / 上下文维护）
                  │
                  ▼
             XuManus（系统提示词 + maxSteps=20）
```

**单次运行流程**（`BaseAgent.runStream()`）：

1. 校验状态必须为 `IDLE` 且提示词非空
2. 状态切换为 `RUNNING`，用户消息写入消息列表
3. 进入步骤循环（最多 `maxSteps` 步，直到 `FINISHED`）：
   - `think()`：拼接下一步提示词 → 调用大模型 → 解析工具调用列表（无工具调用则记录助手消息并结束本步）
   - `act()`：`ToolCallingManager` 执行工具 → 工具结果写回消息上下文 → 若调用 `doTerminate` 则置为 `FINISHED`
   - 每一步结果实时推送至 `SseEmitter`
4. 达到最大步数或任务完成 → 触发 `cleanup()` 清理资源

**关键设计**：

- 通过 `DashScopeChatOptions.withProxyToolCalls(true)` 禁用 Spring AI 内置工具调用，由智能体自行维护消息上下文，避免框架自动执行不可控
- `ToolCallAgent` 只在"无工具调用"时手动记录助手消息；有工具调用时由 `ToolCallingManager` 统一回填，防止上下文重复
- 流式执行放在 `CompletableFuture` 异步线程，避免阻塞 Web 线程，并设置了超时/完成回调

### 对话记忆

| 实现 | 说明 | 启用方式 |
| --- | --- | --- |
| `InMemoryChatMemory` | 内存存储，重启丢失，默认启用 | `LoveApp` 构造器默认 |
| `FileBasedChatMemory` | 基于 Kryo 序列化持久化到 `tmp/chat-memory/{chatId}.kryo` | 替换 `LoveApp` 构造器中的实现 |

### 自定义 Advisor

| Advisor | 作用 |
| --- | --- |
| `MyLoggerAdvisor` | 打印 AI 请求与响应日志（同步 + 流式均支持） |
| `ReReadingAdvisor` | Re2 推理增强：将问题重复两遍再交给模型 |

---

## 安全与注意事项

- **密钥管理**：仓库部分文件（如 `TestApiKey`、`ImageSearchTool`、前端配置）中可能硬编码了 API Key，生产环境请改为环境变量或配置中心注入，并及时轮换已泄露的 Key
- **敏感配置不入库**：`application-local.yml`、`application-prod.yml`、`mcp-servers.json`、`tmp/` 均已在 `.gitignore` 中，克隆后需自行创建本地配置
- **终端工具风险**：`TerminalOperationTool` 可执行任意命令（`cmd.exe /c`），仅建议在可信环境开启，生产环境应移除或加白名单
- **跨域配置**：`CorsConfig` 当前允许任意来源且携带 Cookie（`allowedOriginPatterns("*") + allowCredentials(true)`），生产环境建议收敛为具体域名
- **对话记忆**：默认使用内存存储，多实例部署时各实例记忆不共享，如需共享请替换为 Redis 等集中式 `ChatMemory`
- **模型依赖**：核心链路强依赖阿里云百炼 DashScope 服务，需保证网络可达且额度充足

---

## 致谢

- [Spring AI](https://docs.spring.io/spring-ai/reference/) / [Spring AI Alibaba](https://java2ai.com/)
- [阿里云百炼（DashScope）](https://help.aliyun.com/zh/model-studio/)
- 项目中的恋爱知识库文档仅供学习演示使用

> 本项目为学习实践项目，欢迎交流与改进。
