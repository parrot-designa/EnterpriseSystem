# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在此代码仓库中工作时提供指导。

## 语言策略

**所有 Markdown 文档必须使用中文编写**

- 所有新建的 `.md` 文件必须使用中文
- 技术术语保留英文（如 API、REST、Controller 等）
- 代码注释使用中文
- 文档结构和格式遵循中文技术文档规范

## 开发规范

### Claude Code 文件操作确认规则

在代码开发过程中，Claude Code 需遵循以下文件操作规范：

- **新增文件**：直接创建，无需询问确认
- **修改文件**：在修改前主动询问用户是否需要修改，等待用户确认后再执行
- **删除文件**：在删除前主动通知用户，说明删除原因，等待用户确认后再执行

此规则确保用户对所有文件变更有充分的控制权，同时避免新增文件时的频繁打断。

## 项目概述

这是一个基于 **Spring Boot 2.7.18 和 Java 8** 构建的**多模块 Maven 企业应用**，集成了 **Nacos** 用于服务发现和配置管理。项目采用模块化架构，在不同业务领域之间有清晰的关注点分离。

## 架构设计

### 模块结构

项目由以下 Maven 模块组成：

1. **babystart** - 主应用入口点 (`BabyStartApplication.java`)
   - 聚合所有模块并提供统一启动
   - 包含 `@SpringBootApplication`，扫描 `com.enterprisesystem` 包
   - MyBatis mapper 扫描 `com.enterprisesystem.babymain.mapper`

2. **babymain** - 核心业务逻辑（Seller/商家管理）
   - REST 端点：`/api/v3/seller` (`SellerController.java`)
   - 业务逻辑：`SellerService` 和 `SellerServiceImpl`
   - 领域对象：`SellerDomain` 和 `SellerDomainManager`
   - MyBatis 数据访问：使用 SQL 注解的 `SellerMapper`
   - 数据库：MySQL，包含 `seller` 表（参见 `schema.sql`）

3. **babysecure** - 用户管理实现
   - REST 端点：`/api/v1/users` (`UserController.java`)
   - 完善的单元测试，使用 Mockito (`UserServiceTest.java`)
   - 使用 JPA repository 模式 (`UserRepository`)
   - 通过 `bootstrap.yml` 集成 Nacos 进行服务发现和配置
   - 许可证管理系统，包含 `baby_license` 表

4. **babysecure-api** - API 定义和常量
   - 包含 `SecureApiConstants.java`，定义 API 路径常量
   - 提供 secure 模块的接口定义

5. **babycommon** - 共享工具和通用组件
   - `APIResult.java` - 标准 API 响应包装器
   - `ApiConstants.java` - 通用 API 路径常量
   - `BeanParam.java` - 自定义注解
   - `ApplicationContextProvider` - Spring 上下文访问工具
   - `CommonEnvUtil` - 环境属性管理
   - `BatchCommonAutoConfiguration` - 通过 `spring.factories` 自动配置
   - `LicenseEntity` - 许可证模型定义
   - `BaseMapper<T, ID>` - 通用 Mapper 接口，提供基础 CRUD 方法
   - `BaseService<T, ID>` 和 `BaseServiceImpl` - 通用 Service 接口和实现
   - `PageRequest` 和 `PageResult<T>` - 分页查询支持
   - 参见 `babycommon/src/main/java/com/enterprisesystem/babycommon/mapper/README.md` 使用指南

### 核心设计模式

- **分层架构**：Controller → Service → Mapper/Repository
- **领域驱动设计**：Domain 类和 Domain managers
- **API 版本控制**：`/api/v1/`、`/api/v2/`、`/api/v3/`、`/api/v4/` 端点
- **标准化响应**：所有 API 返回 `APIResult<T>` 包装器
- **混合数据访问**：babysecure 模块使用 JPA，babymain 模块使用 MyBatis
- **Spring Cloud Bootstrap**：使用 `bootstrap.yml` 集成 Nacos（在 `application.yml` 之前加载）
- **自动配置**：通过 `spring.factories` 自定义 Spring Boot 自动配置
- **服务发现**：基于 Nacos 的服务注册和发现

## 开发命令

### 构建和运行

```bash
# 从根目录构建所有模块
mvn clean install

# 构建特定模块（如 babymain）
cd babymain && mvn clean install

# 运行应用
cd babystart && mvn spring-boot:run

# 打包为可执行 JAR
mvn clean package
```

### 测试

```bash
# 运行所有测试
mvn test

# 运行特定模块的测试
cd babysecure && mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceTest

# 运行测试并生成覆盖率报告
mvn clean test jacoco:report
```

### 数据库

- **数据库**：MySQL 8.0.26
- **连接**：`jdbc:mysql://localhost:3306/wjb`
- **凭证**：root/19970920（在 Nacos 中配置）
- **Schema 初始化**：通过 `schema.sql` 文件自动初始化
- **数据表**：
  - `seller` (babymain)：商家管理表，包含自增 ID、code、name、时间戳
  - `baby_license` (babysecure/babycommon)：系统授权表

### Nacos 集成

- **服务器**：`localhost:8848`
- **命名空间**：`dev`（环境隔离）
- **配置管理**：通过 Nacos 配置中心管理外部化配置
- **服务发现**：模块在 Nacos 注册，实现服务间通信
- **Bootstrap 模式**：使用 `spring-cloud-starter-bootstrap` 在 `application.yml` 之前加载 `bootstrap.yml`

### 开发环境搭建

1. **前置要求**：
   - Java 8 JDK
   - Maven 3.6+
   - MySQL 8.0.26 运行在 localhost:3306
   - 已创建数据库 `wjb`
   - Nacos 服务器运行在 localhost:8848（用于服务发现和配置）

2. **IDE 配置**：
   - 推荐使用 IntelliJ IDEA（包含 `.idea/` 目录）
   - 为 Lombok 启用注解处理
   - 为 `BabyStartApplication` 配置 Spring Boot 运行配置

3. **Nacos 设置**（运行前）：
   ```bash
   # 下载并启动 Nacos 服务器
   # 启动 Nacos: sh startup.sh -m standalone
   # 访问控制台: http://localhost:8848/nacos (默认: nacos/nacos)
   # 创建命名空间: dev
   # 在 Nacos 配置中心配置数据库凭证
   ```

## API 结构

### 端点模式

- **用户管理**：`/api/v1/users/**`（babysecure 模块）
- **商家管理**：`/api/v3/seller/**`（babymain 模块）
- **响应格式**：所有端点返回 `APIResult<T>`，包含 `code`、`message`、`data`、`timestamp`

### API 调用示例

```bash
# 分页获取所有商家
GET /api/v3/seller/list?page=1&size=10

# 创建新商家
POST /api/v3/seller/create
Content-Type: application/json
{
  "code": "SELLER001",
  "name": "测试商家"
}
```

## 代码规范

### 命名模式

- **Controllers**：`*Controller.java`（REST 端点）
- **Services**：`*Service.java`（接口）和 `*ServiceImpl.java`（实现）
- **Mappers**：`*Mapper.java`（MyBatis 数据访问）
- **Domain**：`*Domain.java` 和 `*DomainManager.java`
- **Models**：`*Entity.java`（数据库实体）、`*Dto.java`（数据传输对象）

### 通用 Mapper 和 Service 使用规范

为减少重复代码，所有新模块应遵循以下规范：

1. **Mapper 层**：
   - 继承 `com.enterprisesystem.babycommon.mapper.BaseMapper<T, ID>`
   - 使用 MyBatis 注解实现基础 CRUD 方法
   - 只添加特定的业务查询方法

2. **Service 层**：
   - 接口继承 `com.enterprisesystem.babycommon.service.BaseService<T, ID>`
   - 实现类继承 `com.enterprisesystem.babycommon.service.impl.BaseServiceImpl<M, T, ID>`
   - 重写需要自定义的方法（如 `saveOrUpdate()`, `page()` 等）

3. **分页查询**：
   - 使用 `PageRequest` 封装分页参数
   - 使用 `PageResult<T>` 返回分页结果

详细使用指南：`babycommon/src/main/java/com/enterprisesystem/babycommon/mapper/README.md`

### 包结构

```
com.enterprisesystem.{module}/
├── controller/     # REST 控制器
├── service/        # Service 接口
├── impl/           # Service 实现
├── domain/         # 领域对象
├── mapper/         # MyBatis mappers
├── model/          # DTOs 和实体
├── config/         # 配置类
└── constant/       # 常量
```

## 重要文件

- **根 POM**：`/pom.xml` - 父项目配置，包含 Spring Cloud 2021.0.5 和 Spring Cloud Alibaba 2021.0.5.0
- **主应用**：`/babystart/src/main/java/com/enterprisesystem/babystart/BabyStartApplication.java`
- **Nacos Bootstrap**：`/babymain/src/main/resources/bootstrap.yml` 和 `/babysecure/src/main/resources/bootstrap.yml`
- **数据库 Schema**：
  - `/babymain/src/main/resources/schema.sql`（seller 表）
  - `/babycommon/src/main/resources/schema.sql`（baby_license 表）
- **API 响应包装器**：`/babycommon/src/main/java/com/enterprisesystem/babycommon/entity/APIResult.java`
- **自动配置**：`/babycommon/src/main/resources/META-INF/spring.factories`

## 测试策略

- **单元测试**：JUnit 5 + Mockito（参见 `UserServiceTest.java` 示例）
- **测试位置**：每个模块的 `src/test/java/` 目录
- **Mock**：使用 `@Mock` 和 `@InjectMocks` 注解
- **测试结构**：Given-When-Then 模式，包含全面的断言

## 前端集成

- **前端目录**：`/react-antd-admin/`（当前为空）
- **计划**：使用 Ant Design 的 React 管理界面
- **API 集成**：将消费后端模块的 REST 端点

## 故障排查

### 常见问题

1. **数据库连接**：验证 MySQL 运行在 localhost:3306，且存在 `wjb` 数据库
2. **Nacos 连接**：确保 Nacos 服务器运行在 localhost:8848，且已创建 `dev` 命名空间
3. **Bootstrap 配置**：检查是否包含 `spring-cloud-starter-bootstrap` 依赖以集成 Nacos
4. **Lombok 注解**：确保 IDE 已启用注解处理
5. **模块依赖**：检查 `pom.xml` 文件中的版本引用是否正确
6. **MyBatis Mapper 扫描**：验证 `BabyStartApplication` 中的 `@MapperScan` 包含正确的包
7. **配置未加载**：数据库凭证和其他配置必须在 Nacos 配置中心设置，不能在本地配置

### 日志配置

- MyBatis SQL 日志：`logging.level.com.enterprisesystem.babymain.mapper: debug`
- 在 Nacos 配置中心配置日志，便于开发调试
