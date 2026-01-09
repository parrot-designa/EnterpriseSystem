# Baby Gateway - API 网关模块

## 模块简介

Baby Gateway 是基于 Spring Cloud Gateway 构建的统一 API 网关，提供路由转发、请求拦截、日志记录和认证等功能。

## 核心功能

### 1. 全局日志拦截器（GlobalLoggingFilter）

拦截所有请求，记录以下信息：
- 请求方法（GET、POST、PUT、DELETE 等）
- 请求路径
- 请求参数
- 请求头
- 请求体（仅 JSON 类型的 POST/PUT/PATCH 请求）
- 响应状态码
- 请求处理耗时

**位置**：`com.enterprisesystem.babygateway.filter.GlobalLoggingFilter`

**优先级**：-100（最高优先级）

### 2. 全局认证过滤器（GlobalAuthFilter）

拦截所有请求，验证 Token：
- 检查 Authorization 请求头
- 验证 Token 格式（Bearer Token）
- 提取用户信息并传递给下游服务
- 支持白名单路径（登录、注册等）

**位置**：`com.enterprisesystem.babygateway.filter.GlobalAuthFilter`

**优先级**：-99（在日志过滤器之后）

**白名单路径**：
- `/api/v1/users/login` - 用户登录
- `/api/v1/users/register` - 用户注册
- `/actuator/health` - 健康检查
- `/actuator/info` - 应用信息

### 3. 服务路由配置

#### 用户管理服务路由
- **路由 ID**：baby-secure-route
- **目标服务**：baby-secure（通过 Nacos 服务发现）
- **匹配路径**：`/api/v1/users/**`
- **负载均衡**：启用

#### 商家管理服务路由
- **路由 ID**：baby-main-route
- **目标服务**：baby-main（通过 Nacos 服务发现）
- **匹配路径**：`/api/v3/seller/**`
- **负载均衡**：启用

#### 默认路由
- **路由 ID**：baby-default-route
- **目标服务**：baby-secure
- **匹配路径**：`/**`

## 快速开始

### 前置条件

1. 启动 Nacos 服务器（localhost:8848）
2. 在 Nacos 中创建 `dev` 命名空间
3. 确保下游服务（baby-secure、baby-main）已启动并注册到 Nacos

### 启动网关

```bash
# 编译整个项目
mvn clean install

# 启动网关模块
cd babygateway
mvn spring-boot:run
```

网关将在 `8080` 端口启动。

### 测试网关

```bash
# 测试用户管理服务（通过网关访问）
curl -X GET http://localhost:8080/api/v1/users/list

# 测试商家管理服务（通过网关访问）
curl -X GET http://localhost:8080/api/v3/seller/list

# 测试认证（带 Token）
curl -X GET http://localhost:8080/api/v1/users/list \
  -H "Authorization: Bearer your_token_here"
```

## 配置说明

### application.yml

主要配置项：
- **server.port**：网关端口（默认 8080）
- **spring.cloud.gateway.routes**：路由规则配置
- **spring.cloud.gateway.discovery.locator**：服务发现配置
- **logging.level**：日志级别配置

### bootstrap.yml

主要配置项：
- **spring.application.name**：应用名称
- **spring.cloud.nacos.discovery**：Nacos 服务发现配置
- **spring.cloud.nacos.config**：Nacos 配置中心配置

## 过滤器执行顺序

1. **GlobalLoggingFilter**（Order: -100）
   - 记录请求信息
   - 记录请求体（如果有）
   - 记录响应信息和处理耗时

2. **GlobalAuthFilter**（Order: -99）
   - 检查白名单路径
   - 验证 Token
   - 提取用户信息
   - 拒绝未授权请求

## 扩展开发

### 添加新的全局过滤器

创建一个类实现 `GlobalFilter` 和 `Ordered` 接口：

```java
@Component
public class YourCustomFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 你的过滤逻辑
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // 返回过滤器优先级（值越小优先级越高）
        return 0;
    }
}
```

### 添加新的路由规则

在 `application.yml` 中添加：

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: your-service-route
          uri: lb://your-service-name
          predicates:
            - Path=/api/your-path/**
          filters:
            - StripPrefix=0
```

## 注意事项

1. **JWT 验证**：当前 `GlobalAuthFilter` 中的 JWT 验证逻辑需要根据实际项目完善
2. **Token 解析**：`extractUserId()` 和 `extractUserName()` 方法需要实现实际的 JWT 解析逻辑
3. **限流配置**：`application.yml` 中的请求限流配置已注释，如需启用请先配置 Redis
4. **跨域配置**：如需支持跨域请求，请添加 CORS 配置

## 技术栈

- Spring Boot 2.7.18
- Spring Cloud Gateway
- Spring Cloud Alibaba 2021.0.5.0
- Nacos（服务发现 + 配置中心）
- Lombok
- Fastjson

## 常见问题

### 1. 网关无法启动
- 检查 Nacos 服务器是否正常运行
- 检查端口 8080 是否被占用
- 查看日志输出的错误信息

### 2. 请求被拒绝（401 Unauthorized）
- 检查请求是否包含 `Authorization` 请求头
- 检查 Token 格式是否正确（`Bearer your_token`）
- 确认请求路径是否在白名单中

### 3. 无法路由到下游服务
- 确认下游服务已启动
- 确认下游服务已注册到 Nacos
- 检查路由规则配置是否正确

## 后续优化

- [ ] 完善 JWT Token 验证逻辑
- [ ] 添加请求限流功能
- [ ] 添加熔断降级机制
- [ ] 添加跨域支持
- [ ] 添加请求/响应日志持久化
- [ ] 添加监控和告警功能
