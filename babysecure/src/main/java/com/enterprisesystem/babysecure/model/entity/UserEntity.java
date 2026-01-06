package com.enterprisesystem.babysecure.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体类
 * 对应数据库表：b_user
 *
 * Entity 类的设计原则：
 * 1. 字段名称与数据库表字段保持一致（遵循驼峰命名规则）
 * 2. 只包含数据库表中存在的字段
 * 3. 用于 Mapper 层与数据库交互
 *
 * @Data：自动生成 getter/setter/toString/equals/hashCode
 * @NoArgsConstructor：生成无参构造函数（MyBatis 反射需要）
 * @AllArgsConstructor：生成全参构造函数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    // ==================== 主键 ====================

    /**
     * 主键ID
     * 数据库字段：id INT(11) NOT NULL COMMENT '主键 id'
     * 注意：此表未使用 AUTO_INCREMENT，需手动生成ID
     */
    private int id;

    // ==================== 基础信息 ====================

    /**
     * 账号
     * 数据库字段：account VARCHAR(64) DEFAULT NULL COMMENT '账号'
     * 用于用户登录，建议配合唯一索引使用
     */
    private String account;

    /**
     * 姓名
     * 数据库字段：name VARCHAR(128) DEFAULT NULL COMMENT '姓名'
     * 用户真实姓名
     */
    private String name;

    /**
     * 邮箱
     * 数据库字段：email VARCHAR(64) DEFAULT NULL COMMENT '邮箱'
     * 用于找回密码或接收通知
     */
    private String email;

    /**
     * 手机号
     * 数据库字段：telephone VARCHAR(32) DEFAULT NULL COMMENT '手机号'
     * 用于短信验证或登录
     */
    private String telephone;

    // ==================== 密码相关 ====================

    /**
     * 登录密码
     * 数据库字段：password VARCHAR(64) DEFAULT NULL COMMENT '密码'
     * 注意：应存储加密后的哈希值（如 BCrypt），而非明文密码
     */
    private String password;

    /**
     * 密码有效日期
     * 数据库字段：pwd_valid_date datetime DEFAULT NULL COMMENT '密码有效日期'
     * 用于强制定期修改密码策略
     */
    private java.util.Date pwdValidDate;

    /**
     * 登录失败次数
     * 数据库字段：login_fail_count int(11) DEFAULT '0' COMMENT '登录失败次数'
     * 用于账户安全锁定机制（如连续失败5次锁定账户）
     */
    private Integer loginFailCount;

    /**
     * 密码修改日志
     * 数据库字段：pwd_log text DEFAULT NULL COMMENT '密码修改日志'
     * 记录密码修改历史，防止重复使用旧密码
     * 可能格式：JSON 或自定义格式
     */
    private String pwdLog;

    /**
     * 签名密码
     * 数据库字段：signature_pwd varchar(128) DEFAULT NULL COMMENT '签名密码'
     * 用于电子签名等特殊场景的独立密码
     */
    private String signaturePwd;

    /**
     * 签名密码有效日期
     * 数据库字段：c_signture_pwd_valid_date datetime DEFAULT NULL
     * 签名密码的有效期
     */
    private java.util.Date cSignturePwdValidDate;

    /**
     * 签名密码修改日志
     * 数据库字段：c_signture_pwd_log text DEFAULT NULL COMMENT '电子签名密码修改日志'
     * 签名密码的修改历史记录
     */
    private String cSignturePwdLog;

    // ==================== 状态控制 ====================

    /**
     * 用户状态
     * 数据库字段：c_status int(11) NOT NULL COMMENT '用户状态'
     * 可能的值：
     * - 1: 正常
     * - 0: 禁用
     * - 2: 锁定
     * - 其他自定义状态
     */
    private Integer cStatus;

    /**
     * 账户有效时间
     * 数据库字段：account_valid_date datetime DEFAULT NULL COMMENT '账户有效时间'
     * 用于临时账户或定期审核机制
     */
    private java.util.Date accountValidDate;

    // ==================== 版本控制与唯一标识 ====================

    /**
     * 修订号
     * 数据库字段：revision int(11) DEFAULT '1' COMMENT '修订号'
     * 用于乐观锁控制，防止并发修改冲突
     * 每次更新时 +1
     */
    private Integer revision;

    /**
     * UUID
     * 数据库字段：uuid binary(16) DEFAULT NULL COMMENT 'uuid'
     * 用户的唯一标识符（128位）
     * 注意：数据库存储为 BINARY(16)，Java 中使用 byte[] 映射
     */
    private byte[] uuid;

    /**
     * 快照 UUID
     * 数据库字段：c_his_uuid binary(16) DEFAULT NULL COMMENT '快照 uuid'
     * 用于数据追溯和历史版本管理
     */
    private byte[] cHisUuid;

    // ==================== 扩展信息 ====================

    /**
     * 扩展信息
     * 数据库字段：c_extend_info text DEFAULT NULL COMMENT '扩展信息'
     * JSON 格式的扩展数据，灵活存储额外的业务属性
     * 示例：{"department": "技术部", "position": "工程师"}
     */
    private String cExtendInfo;

    /**
     * 头像
     * 数据库字段：c_image text DEFAULT NULL COMMENT '头像'
     * 可能存储：
     * - 图片的 Base64 编码
     * - 图片 URL
     * - 图片文件路径
     */
    private String cImage;
}
