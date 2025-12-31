package com.enterprisesystem.babycommon.mq.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

/**
 * 消息消费者基类
 *
 * 【功能说明】
 * - 提供消息消费的通用方法
 * - 手动确认消息（ACK）
 * - 消息重试机制
 *
 * 【使用示例】
 * <pre>
 * {@code
 * @Component
 * public class OrderMessageConsumer extends BaseMessageConsumer {
 *
 *     @Override
 *     @RabbitListener(queues = "queue.order")
 *     public void onMessage(Message message, Channel channel) throws Exception {
 *         try {
 *             // 1. 获取消息内容
 *             String messageBody = new String(message.getBody());
 *
 *             // 2. 处理业务逻辑
 *             processOrder(messageBody);
 *
 *             // 3. 手动确认消息
 *             ackMessage(message, channel);
 *
 *         } catch (Exception e) {
 *             // 4. 处理失败，拒绝消息（重新入队）
 *             nackMessage(message, channel, true);
 *         }
 *     }
 *
 *     private void processOrder(String message) {
 *         // 处理订单逻辑
 *     }
 * }
 * }
 * </pre>
 *
 * @author Claude Code
 */
public abstract class BaseMessageConsumer {

    /**
     * 手动确认消息
     *
     * 【作用】
     * 告诉 RabbitMQ 消息已成功处理，可以从队列中移除
     *
     * @param message 消息对象
     * @param channel RabbitMQ 通道
     */
    protected void ackMessage(Message message, Channel channel) {
        try {
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            channel.basicAck(deliveryTag, false);
            // 参数1：消息投递序号
            // 参数2：是否批量确认（false = 只确认当前消息）

            System.out.println("✅ 消息确认成功，序号：" + deliveryTag);
        } catch (Exception e) {
            System.err.println("❌ 消息确认失败：" + e.getMessage());
        }
    }

    /**
     * 拒绝消息
     *
     * 【作用】
     * 告诉 RabbitMQ 消息处理失败
     *
     * @param message 消息对象
     * @param channel RabbitMQ 通道
     * @param requeue 是否重新入队
     *                - true：消息返回队列，重新消费
     *                - false：消息被丢弃（如果配置了死信队列，会进入死信队列）
     */
    protected void nackMessage(Message message, Channel channel, boolean requeue) {
        try {
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            channel.basicNack(deliveryTag, false, requeue);
            // 参数1：消息投递序号
            // 参数2：是否批量拒绝（false = 只拒绝当前消息）
            // 参数3：是否重新入队

            System.out.println("❌ 消息拒绝，序号：" + deliveryTag + "，是否重新入队：" + requeue);
        } catch (Exception e) {
            System.err.println("❌ 消息拒绝失败：" + e.getMessage());
        }
    }

    /**
     * 拒绝消息并重新入队
     *
     * 【使用场景】
     * - 临时性错误（如数据库连接失败）
     * - 希望稍后重试
     *
     * @param message 消息对象
     * @param channel RabbitMQ 通道
     */
    protected void rejectAndRequeue(Message message, Channel channel) {
        nackMessage(message, channel, true);
    }

    /**
     * 拒绝消息并丢弃（进入死信队列）
     *
     * 【使用场景】
     * - 消息格式错误
     * - 无法恢复的错误
     *
     * @param message 消息对象
     * @param channel RabbitMQ 通道
     */
    protected void rejectAndDiscard(Message message, Channel channel) {
        nackMessage(message, channel, false);
    }
}
