package com.enterprisesystem.babycommon.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置类
 *
 * 【功能说明】
 * - 定义交换机（Exchange）
 * - 定义队列（Queue）
 * - 绑定交换机和队列
 *
 * 【核心概念】
 * 1. Exchange（交换机）：接收消息并路由到队列
 *    - Direct：精确匹配路由键
 *    - Fanout：广播到所有绑定队列
 *    - Topic：模糊匹配路由键
 *
 * 2. Queue（队列）：存储消息，等待消费者消费
 *
 * 3. Binding（绑定）：交换机和队列的绑定关系
 *
 * 4. RoutingKey（路由键）：决定消息路由到哪个队列
 *
 * @author Claude Code
 */
@Configuration
public class RabbitMQConfig {

    // ==================== 交换机名称 ====================

    /**
     * 直连交换机（用于精确路由）
     * 例如：订单消息、用户消息
     */
    public static final String EXCHANGE_DIRECT = "exchange.direct.system";

    /**
     * 扇形交换机（用于广播）
     * 例如：系统通知、日志广播
     */
    public static final String EXCHANGE_FANOUT = "exchange.fanout.notification";

    /**
     * 主题交换机（用于模糊匹配）
     * 例如：按模块路由（order.*、user.*）
     */
    public static final String EXCHANGE_TOPIC = "exchange.topic.business";

    // ==================== 队列名称 ====================

    /**
     * 系统消息队列（通用）
     */
    public static final String QUEUE_SYSTEM = "queue.system";

    /**
     * 订单队列
     */
    public static final String QUEUE_ORDER = "queue.order";

    /**
     * 部门队列
     */
    public static final String QUEUE_DEPARTMENT = "queue.department";

    /**
     * 通知队列（邮件、短信）
     */
    public static final String QUEUE_NOTIFICATION = "queue.notification";

    /**
     * 死信队列（处理失败消息）
     */
    public static final String QUEUE_DLQ = "queue.dead.letter";

    // ==================== 路由键 ====================

    /**
     * 系统消息路由键
     */
    public static final String ROUTING_KEY_SYSTEM = "system.message";

    /**
     * 订单创建路由键
     */
    public static final String ROUTING_KEY_ORDER_CREATE = "order.create";

    /**
     * 部门变更路由键
     */
    public static final String ROUTING_KEY_DEPARTMENT_CHANGE = "department.change";

    // ==================== 创建交换机 ====================

    /**
     * 创建直连交换机
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_DIRECT, true, false);
        // 参数1：交换机名称
        // 参数2：是否持久化（true = 服务器重启后仍存在）
        // 参数3：是否自动删除（没有队列绑定时是否删除）
    }

    /**
     * 创建扇形交换机
     *
     * @return FanoutExchange
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_FANOUT, true, false);
    }

    /**
     * 创建主题交换机
     *
     * @return TopicExchange
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_TOPIC, true, false);
    }

    // ==================== 创建队列 ====================

    /**
     * 创建系统消息队列
     *
     * @return Queue
     */
    @Bean
    public Queue systemQueue() {
        return QueueBuilder.durable(QUEUE_SYSTEM).build();
    }

    /**
     * 创建订单队列（带死信队列）
     *
     * @return Queue
     */
    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(QUEUE_ORDER)
                .withArgument("x-dead-letter-exchange", EXCHANGE_DIRECT)  // 死信交换机
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY_SYSTEM)  // 死信路由键
                .build();
    }

    /**
     * 创建部门队列
     *
     * @return Queue
     */
    @Bean
    public Queue departmentQueue() {
        return QueueBuilder.durable(QUEUE_DEPARTMENT).build();
    }

    /**
     * 创建通知队列
     *
     * @return Queue
     */
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(QUEUE_NOTIFICATION).build();
    }

    /**
     * 创建死信队列
     *
     * @return Queue
     */
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(QUEUE_DLQ).build();
    }

    // ==================== 绑定关系 ====================

    /**
     * 系统队列绑定到直连交换机
     *
     * @return Binding
     */
    @Bean
    public Binding systemBinding() {
        return BindingBuilder.bind(systemQueue())
                .to(directExchange())
                .with(ROUTING_KEY_SYSTEM);
    }

    /**
     * 订单队列绑定到直连交换机
     *
     * @return Binding
     */
    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue())
                .to(directExchange())
                .with(ROUTING_KEY_ORDER_CREATE);
    }

    /**
     * 部门队列绑定到直连交换机
     *
     * @return Binding
     */
    @Bean
    public Binding departmentBinding() {
        return BindingBuilder.bind(departmentQueue())
                .to(directExchange())
                .with(ROUTING_KEY_DEPARTMENT_CHANGE);
    }

    /**
     * 通知队列绑定到扇形交换机（广播）
     *
     * @return Binding
     */
    @Bean
    public Binding notificationBinding() {
        return BindingBuilder.bind(notificationQueue())
                .to(fanoutExchange());
    }
}
