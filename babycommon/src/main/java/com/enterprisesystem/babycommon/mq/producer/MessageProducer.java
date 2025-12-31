package com.enterprisesystem.babycommon.mq.producer;

import com.enterprisesystem.babycommon.mq.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.UUID;

/**
 * 消息生产者工具类
 *
 * 【功能说明】
 * - 发送消息到 RabbitMQ
 * - 支持同步和异步发送
 * - 支持消息确认机制
 *
 * 【使用示例】
 * <pre>
 * {@code
 * @Resource
 * private MessageProducer messageProducer;
 *
 * // 发送对象消息
 * messageProducer.sendMessage("order.create", orderDto);
 *
 * // 发送字符串消息
 * messageProducer.sendMessage("system.message", "Hello RabbitMQ");
 * }
 * </pre>
 *
 * @author Claude Code
 */
@Component
public class MessageProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 初始化回调函数
     * 在消息发送到交换机后触发回调
     */
    @PostConstruct
    public void init() {
        // 消息发送到交换机的回调（成功或失败）
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                // 消息成功发送到交换机
                System.out.println("✅ 消息成功发送到交换机");

                // 可以在这里记录日志或更新数据库
                if (correlationData != null) {
                    String messageId = correlationData.getId();
                    System.out.println("消息ID：" + messageId);
                }
            } else {
                // 消息发送到交换机失败
                System.err.println("❌ 消息发送到交换机失败：" + cause);

                // 可以在这里进行重试或记录失败日志
            }
        });

        // 消息从交换机路由到队列的回调（失败时触发）
        rabbitTemplate.setReturnsCallback(returned -> {
            System.err.println("❌ 消息未路由到队列");
            System.err.println("交换机：" + returned.getExchange());
            System.err.println("路由键：" + returned.getRoutingKey());
            System.err.println("消息：" + returned.getMessage());
            System.err.println("回复码：" + returned.getReplyCode());
            System.err.println("回复文本：" + returned.getReplyText());

            // 可以在这里进行重试或记录失败日志
        });
    }

    /**
     * 发送消息到直连交换机（同步方式）
     *
     * 【适用场景】
     * - 需要立即确认消息是否发送成功
     * - 重要消息（如订单、支付）
     *
     * @param routingKey 路由键（如：order.create）
     * @param message 消息内容（可以是任意对象）
     */
    public void sendMessage(String routingKey, Object message) {
        // 生成唯一消息ID（用于消息确认）
        String messageId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(messageId);

        try {
            // 发送消息
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_DIRECT,  // 交换机
                    routingKey,                        // 路由键
                    message,                           // 消息内容
                    correlationData                    // 消息ID（用于回调）
            );

            System.out.println("📤 发送消息 - 路由键：" + routingKey + "，消息：" + message);
        } catch (Exception e) {
            System.err.println("❌ 发送消息失败：" + e.getMessage());
            throw new RuntimeException("发送消息失败", e);
        }
    }

    /**
     * 发送消息到主题交换机（支持通配符）
     *
     * 【路由键通配符规则】
     * - * ：匹配一个单词（如：order.* 匹配 order.create）
     * - # ：匹配零个或多个单词（如：order.# 匹配 order.create.payment）
     *
     * 【使用示例】
     * <pre>
     * {@code
     * // 发送到订单创建队列
     * messageProducer.sendMessageToTopic("order.create", orderDto);
     *
     * // 发送到所有订单相关队列
     * messageProducer.sendMessageToTopic("order.#", orderDto);
     * }
     * </pre>
     *
     * @param routingKey 路由键（支持通配符）
     * @param message 消息内容
     */
    public void sendMessageToTopic(String routingKey, Object message) {
        String messageId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(messageId);

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_TOPIC,
                    routingKey,
                    message,
                    correlationData
            );

            System.out.println("📤 发送消息到主题交换机 - 路由键：" + routingKey);
        } catch (Exception e) {
            System.err.println("❌ 发送消息失败：" + e.getMessage());
            throw new RuntimeException("发送消息失败", e);
        }
    }

    /**
     * 广播消息到所有绑定队列
     *
     * 【适用场景】
     * - 系统通知
     * - 缓存刷新
     * - 日志广播
     *
     * 【使用示例】
     * <pre>
     * {@code
     * // 广播系统通知
     * messageProducer.broadcastMessage("系统将在今晚22:00进行维护");
     * }
     * </pre>
     *
     * @param message 消息内容
     */
    public void broadcastMessage(Object message) {
        String messageId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(messageId);

        try {
            // 扇形交换机会忽略路由键，将消息广播到所有绑定的队列
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_FANOUT,
                    "",  // 扇形交换机不需要路由键
                    message,
                    correlationData
            );

            System.out.println("📢 广播消息：" + message);
        } catch (Exception e) {
            System.err.println("❌ 广播消息失败：" + e.getMessage());
            throw new RuntimeException("广播消息失败", e);
        }
    }

    /**
     * 发送延迟消息（需要安装延迟插件）
     *
     * 【注意】
     * 需要安装 RabbitMQ 延迟插件：rabbitmq_delayed_message_exchange
     *
     * @param routingKey 路由键
     * @param message 消息内容
     * @param delayMillis 延迟时间（毫秒）
     */
    public void sendDelayedMessage(String routingKey, Object message, long delayMillis) {
        // TODO: 实现延迟消息（需要安装延迟插件）
        System.out.println("⚠️ 延迟消息功能需要安装 RabbitMQ 延迟插件");
        throw new UnsupportedOperationException("延迟消息功能暂未实现");
    }
}
