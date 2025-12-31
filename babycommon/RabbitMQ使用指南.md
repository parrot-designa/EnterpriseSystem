# RabbitMQ 使用指南

## 📋 目录
1. [环境准备](#环境准备)
2. [快速开始](#快速开始)
3. [使用示例](#使用示例)
4. [常见问题](#常见问题)

---

## 一、环境准备

### 1.1 安装 RabbitMQ

#### **方式 1：Docker（推荐）**

```bash
# 拉取 RabbitMQ 镜像（带管理界面）
docker pull rabbitmq:management

# 启动容器
docker run -d --name rabbitmq \
  -p 5672:5672 \      # AMQP 协议端口
  -p 15672:15672 \    # 管理界面端口
  rabbitmq:management

# 查看日志
docker logs -f rabbitmq
```

#### **方式 2：Homebrew（macOS）**

```bash
# 安装
brew install rabbitmq

# 启动
brew services start rabbitmq

# 启用管理界面
rabbitmq-plugins enable rabbitmq_management
```

### 1.2 访问管理界面

```
URL: http://localhost:15672
用户名: guest
密码: guest
```

### 1.3 在 Nacos 中配置 RabbitMQ 连接

在 Nacos 配置中心添加配置：

```yaml
# Data ID: babysecure.yml 或 babymain.yml
# Group: DEFAULT_GROUP

spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    virtual-host: /
    listener:
      simple:
        acknowledge-mode: manual  # 手动确认模式
        prefetch: 1               # 每次拉取一条消息
        concurrency: 5            # 并发消费者数量
        max-concurrency: 10       # 最大并发数
```

---

## 二、快速开始

### 2.1 在模块中添加依赖

如果模块还未添加 RabbitMQ 依赖，在 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>com.enterprisesystem</groupId>
    <artifactId>babycommon</artifactId>
    <version>1.0.0</version>
</dependency>
```

**babycommon 已经包含了 RabbitMQ 依赖，其他模块依赖 babycommon 后即可使用。**

### 2.2 发送消息（生产者）

```java
package com.enterprisesystem.babymain.controller;

import com.enterprisesystem.babycommon.mq.producer.MessageProducer;
import com.enterprisesystem.babycommon.mq.dto.SystemMessage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v3/test")
public class TestController {

    @Resource
    private MessageProducer messageProducer;

    /**
     * 测试发送消息
     */
    @PostMapping("/sendMessage")
    public String sendMessage() {
        // 创建系统消息
        SystemMessage message = new SystemMessage();
        message.setMessageId("MSG-" + System.currentTimeMillis());
        message.setMessageType("TEST_MESSAGE");
        message.setTitle("测试消息");
        message.setContent("这是一条测试消息");
        message.setSender("BABYMAIN_SERVICE");
        message.setPriority(5);
        message.setCreateTime(new Date());

        // 发送消息
        messageProducer.sendMessage("system.message", message);

        return "消息已发送！";
    }
}
```

### 2.3 接收消息（消费者）

```java
package com.enterprisesystem.babysecure.consumer;

import com.enterprisesystem.babycommon.mq.consumer.BaseMessageConsumer;
import com.enterprisesystem.babycommon.mq.dto.SystemMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

/**
 * 系统消息消费者
 */
@Component
public class SystemMessageConsumer extends BaseMessageConsumer {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 监听系统队列
     */
    @RabbitListener(queues = "queue.system")
    public void consumeSystemMessage(Message message, Channel channel) {
        try {
            // 1. 解析消息
            String messageBody = new String(message.getBody());
            System.out.println("📥 接收到消息：" + messageBody);

            // 2. 转换为对象
            SystemMessage systemMessage = objectMapper.readValue(messageBody, SystemMessage.class);
            System.out.println("📨 消息标题：" + systemMessage.getTitle());
            System.out.println("📝 消息内容：" + systemMessage.getContent());

            // 3. 处理业务逻辑
            processMessage(systemMessage);

            // 4. 手动确认消息
            ackMessage(message, channel);

            System.out.println("✅ 消息处理成功");

        } catch (Exception e) {
            System.err.println("❌ 消息处理失败：" + e.getMessage());
            e.printStackTrace();

            // 拒绝消息并重新入队（稍后重试）
            rejectAndRequeue(message, channel);
        }
    }

    /**
     * 处理消息业务逻辑
     */
    private void processMessage(SystemMessage message) {
        // TODO: 根据消息类型处理不同的业务逻辑

        switch (message.getMessageType()) {
            case "TEST_MESSAGE":
                System.out.println("处理测试消息...");
                break;
            case "ORDER_CREATE":
                System.out.println("处理订单创建消息...");
                break;
            case "DEPARTMENT_CHANGE":
                System.out.println("处理部门变更消息...");
                break;
            default:
                System.out.println("未知消息类型：" + message.getMessageType());
        }
    }
}
```

---

## 三、使用示例

### 3.1 场景 1：部门变更通知

**需求**：当部门信息变更时，通知所有相关系统

#### **步骤 1：发送消息**

在 `DepartmentServiceImpl` 中添加：

```java
@Resource
private MessageProducer messageProducer;

@Override
public DepartmentDto updateDepartment(DepartmentDto departmentDto) {
    // 1. 更新数据库
    DepartmentEntity entity = dtoToEntity(departmentDto);
    departmentMapper.update(entity);

    // 2. 发送变更通知
    SystemMessage message = new SystemMessage();
    message.setMessageId("DEPT-" + System.currentTimeMillis());
    message.setMessageType("DEPARTMENT_CHANGE");
    message.setTitle("部门信息变更");
    message.setContent("部门【" + departmentDto.getName() + "】信息已更新");
    message.setSender("DEPARTMENT_SERVICE");
    message.setCreateTime(new Date());

    // 设置扩展数据
    HashMap<String, Object> extData = new HashMap<>();
    extData.put("departmentId", departmentDto.getId());
    extData.put("oldName", "技术部");
    extData.put("newName", departmentDto.getName());
    message.setExtData(extData);

    // 发送消息
    messageProducer.sendMessage("department.change", message);

    return entityToDto(entity);
}
```

#### **步骤 2：消费消息**

```java
@Component
public class DepartmentChangeConsumer extends BaseMessageConsumer {

    @RabbitListener(queues = "queue.department")
    public void consumeDepartmentChange(Message message, Channel channel) {
        try {
            String messageBody = new String(message.getBody());
            SystemMessage systemMessage = new ObjectMapper().readValue(messageBody, SystemMessage.class);

            System.out.println("收到部门变更通知：" + systemMessage.getContent());

            // 处理业务逻辑
            // - 更新缓存
            // - 通知相关用户
            // - 记录审计日志

            ackMessage(message, channel);
        } catch (Exception e) {
            rejectAndRequeue(message, channel);
        }
    }
}
```

### 3.2 场景 2：订单异步处理

```java
@RestController
@RequestMapping("/api/v3/order")
public class OrderController {

    @Resource
    private MessageProducer messageProducer;

    /**
     * 创建订单（异步处理）
     */
    @PostMapping
    public APIResult<String> createOrder(@RequestBody OrderDto orderDto) {
        // 1. 保存订单到数据库
        // Order order = orderService.save(orderDto);

        // 2. 发送消息到 MQ（异步处理后续流程）
        messageProducer.sendMessage("order.create", orderDto);

        // 3. 立即返回成功
        return new APIResult<>("订单创建成功，后续处理中...");
    }
}
```

消费者异步处理：

```java
@Component
public class OrderConsumer extends BaseMessageConsumer {

    @RabbitListener(queues = "queue.order")
    public void consumeOrderCreate(Message message, Channel channel) {
        try {
            OrderDto order = new ObjectMapper().readValue(
                new String(message.getBody()),
                OrderDto.class
            );

            // 异步处理
            // 1. 扣减库存
            // 2. 发送积分
            // 3. 发送优惠券
            // 4. 发送通知

            ackMessage(message, channel);
        } catch (Exception e) {
            rejectAndRequeue(message, channel);
        }
    }
}
```

---

## 四、常见问题

### Q1: 消息丢失了怎么办？

**原因**：
- 交换机或队列未持久化
- 消费者自动确认，但处理失败
- RabbitMQ 服务器重启

**解决方案**：
```yaml
spring:
  rabbitmq:
    # 生产者确认
    publisher-confirm-type: correlated
    publisher-returns: true
    # 消费者手动确认
    listener:
      simple:
        acknowledge-mode: manual
```

### Q2: 消息重复消费怎么办？

**解决方案**：使用 Redis 实现幂等性

```java
@RabbitListener(queues = "queue.order")
public void consumeOrder(String orderId) {
    // 检查是否已处理
    String key = "order:processed:" + orderId;
    Boolean isProcessed = redisTemplate.hasKey(key);

    if (Boolean.TRUE.equals(isProcessed)) {
        return;  // 已处理，跳过
    }

    // 处理业务
    // ...

    // 标记已处理
    redisTemplate.opsForValue().set(key, "processed", 24, TimeUnit.HOURS);
}
```

### Q3: 如何查看队列中的消息？

**方法 1**：通过管理界面
- 访问 http://localhost:15672
- Queues → 选择队列 → Get Messages

**方法 2**：通过代码
```java
@Resource
private RabbitAdmin rabbitAdmin;

public long getMessageCount(String queueName) {
    return rabbitAdmin.getQueueInfo(queueName).getMessageCount();
}
```

### Q4: 消息积压了怎么办？

**解决方案**：
1. 增加消费者数量
2. 批量处理消息
3. 优化处理逻辑

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        concurrency: 10      # 增加并发数
        max-concurrency: 20  # 增加最大并发数
        prefetch: 5          # 每次拉取多条消息
```

---

## 五、测试验证

### 5.1 启动项目

```bash
# 确保 RabbitMQ 已启动
docker ps | grep rabbitmq

# 启动 Spring Boot 应用
cd babystart
mvn spring-boot:run
```

### 5.2 测试发送消息

使用 Apifox 或 Postman：

```bash
POST http://localhost:8080/api/v3/test/sendMessage
```

### 5.3 查看消息

**方式 1**：查看控制台日志

```log
📤 发送消息 - 路由键：system.message，消息：SystemMessage(...)
✅ 消息成功发送到交换机
📥 接收到消息：{"messageId":"...","title":"测试消息",...}
✅ 消息处理成功
```

**方式 2**：查看 RabbitMQ 管理界面

```
http://localhost:15672
→ Queues → queue.system → Get Messages
```

---

## 六、最佳实践

### 6.1 消息设计原则

| 原则 | 说明 | 示例 |
|------|------|------|
| **幂等性** | 消息重复消费不影响结果 | 使用唯一ID去重 |
| **可靠性** | 确保消息不丢失 | 持久化 + 手动ACK |
| **可追溯** | 记录消息流转日志 | 每个环节记录日志 |
| **异步处理** | 非核心逻辑异步处理 | 发送邮件、短信 |

### 6.2 性能优化

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        prefetch: 10         # 预取数量
        concurrency: 5       # 最小并发
        max-concurrency: 10  # 最大并发
        batch: true          # 批量消费
```

### 6.3 监控告警

- 监控队列消息数量
- 监控消费速率
- 设置死信队列告警
- 记录消息处理日志

---

**文档完成！** 🎉
