package com.enterprisesystem.babycommon.mq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统消息实体
 *
 * 【用途】
 * - 系统通知
 * - 模块间通信
 * - 异步任务消息
 *
 * @author Claude Code
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID（唯一标识）
     */
    private String messageId;

    /**
     * 消息类型
     * 例如：ORDER_CREATE、DEPARTMENT_CHANGE、USER_LOGIN
     */
    private String messageType;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送者
     * 例如：ORDER_SERVICE、DEPARTMENT_SERVICE
     */
    private String sender;

    /**
     * 接收者（可选）
     * 例如：USER_SERVICE、NOTIFICATION_SERVICE
     */
    private String receiver;

    /**
     * 业务数据（JSON格式）
     */
    private String businessData;

    /**
     * 优先级（1-10，数字越大优先级越高）
     */
    private Integer priority;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 扩展数据（Map格式，灵活存储）
     */
    private java.util.Map<String, Object> extData;
}
