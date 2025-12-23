# 用户管理模块 (User Management Module)

本项目实现了完整的用户管理CRUD功能，采用分层架构设计，分为API定义模块和实现模块。

## 项目结构

```
enterprise-system/
├── babysecure-api/          # API定义模块
│   ├── src/main/java/com/enterprisesystem/
│   │   ├── api/
│   │   │   └── UserApi.java         # 用户API接口定义
│   │   ├── model/
│   │   │   └── User.java            # 用户实体类
│   │   └── repository/
│   └── pom.xml
│
├── babysecure/              # API实现模块
│   ├── src/main/java/com/enterprisesystem/
│   │   ├── controller/
│   │   │   └── UserController.java  # REST控制器，实现UserApi
│   │   ├── service/
│   │   │   └── UserService.java     # 业务逻辑服务
│   │   ├── repository/
│   │   │   └── UserRepository.java  # 数据访问层
│   │   ├── config/
│   │   │   └── UserConfig.java      # 配置类
│   │   └── BabySecureApplication.java # Spring Boot启动类
│   ├── src/main/resources/
│   │   └── application.properties   # 应用配置
│   └── pom.xml
│
└── pom.xml                  # 父模块pom.xml
```

## 功能特性

### babysecure-api 模块

该模块定义了用户管理的API接口和实体类：

1. **User 实体类**
   - 包含用户基本信息：用户名、邮箱、密码、姓名、电话等
   - 支持数据验证注解
   - 包含创建和更新时间戳
   - JPA注解支持

2. **UserApi 接口**
   定义了完整的REST API：
   - `POST /api/users` - 创建用户
   - `PUT /api/users/{id}` - 更新用户
   - `GET /api/users/{id}` - 根据ID获取用户
   - `GET /api/users` - 分页获取所有用户
   - `GET /api/users/list` - 获取所有用户（无分页）
   - `DELETE /api/users/{id}` - 删除用户
   - `GET /api/users/username/{username}` - 根据用户名获取用户
   - `GET /api/users/email/{email}` - 根据邮箱获取用户
   - `GET /api/users/exists/username/{username}` - 检查用户名是否存在
   - `GET /api/users/exists/email/{email}` - 检查邮箱是否存在
   - `PATCH /api/users/{id}/status` - 启用/禁用用户

### babysecure 模块

该模块实现了UserApi接口：

1. **UserRepository**
   - 扩展JpaRepository
   - 提供用户名、邮箱查询方法
   - 支持分页查询
   - 自定义查询方法

2. **UserService**
   - 实现所有业务逻辑
   - 数据验证和错误处理
   - 事务管理
   - 重复数据检查

3. **UserController**
   - 实现UserApi接口
   - RESTful控制器
   - 统一异常处理
   - HTTP状态码管理

## 构建和运行

### 1. 编译整个项目

```bash
mvn clean compile
```

### 2. 运行测试

```bash
mvn test
```

### 3. 启动应用

```bash
cd babysecure
mvn spring-boot:run
```

### 4. 访问应用

- 应用启动后，API服务运行在：http://localhost:8080
- H2控制台：http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:userdb`
  - Username: `sa`
  - Password: `（空）`

## API 使用示例

### 创建用户

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "123-456-7890"
  }'
```

### 获取用户列表（分页）

```bash
curl "http://localhost:8080/api/users?page=0&size=10"
```

### 根据ID获取用户

```bash
curl http://localhost:8080/api/users/1
```

### 更新用户

```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john.updated@example.com",
    "password": "newpassword123",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "123-456-7890"
  }'
```

### 删除用户

```bash
curl -X DELETE http://localhost:8080/api/users/1
```

### 检查用户名是否存在

```bash
curl http://localhost:8080/api/users/exists/username/johndoe
```

### 启用/禁用用户

```bash
curl -X PATCH http://localhost:8080/api/users/1/status?enabled=false
```

## 技术栈

- **Spring Boot 2.7.18**
- **Spring Data JPA**
- **H2 Database** (内存数据库)
- **Hibernate**
- **Maven**
- **Java 8**

## 数据库配置

默认使用H2内存数据库，配置在 `babysecure/src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:h2:mem:userdb
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

## 扩展说明

1. **更换数据库**：在babysecure模块的pom.xml中添加相应数据库驱动依赖，并修改application.properties配置。

2. **添加安全认证**：可以在UserController上添加Spring Security注解和JWT token验证。

3. **添加日志**：已经配置了SQL日志，可以在application.properties中调整日志级别。

4. **添加缓存**：可以使用Spring Cache注解在UserService方法上添加缓存支持。

## 作者

Enterprise System Team
