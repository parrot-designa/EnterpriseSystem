package com.enterprisesystem.babysecure.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 部门实体类
 * 对应数据库表：department
 *
 * Entity 类的设计原则：
 * 1. 字段名称与数据库表字段保持一致（或遵循驼峰命名规则）
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
public class DepartmentEntity {

    // ==================== 主键 ====================

    /**
     * 部门ID
     * 数据库字段：id INT PRIMARY KEY AUTO_INCREMENT
     * 自增主键，插入数据时不需要指定值
     */
    private Integer id;

    // ==================== 业务字段 ====================

    /**
     * 部门编码
     * 数据库字段：code VARCHAR(50) NOT NULL UNIQUE
     * 例如：DEPT001, DEPT002
     */
    private String code;

    /**
     * 部门名称
     * 数据库字段：name VARCHAR(100) NOT NULL
     * 例如：技术部、市场部
     */
    private String name;

    /**
     * 父部门ID
     * 数据库字段：parent_id INT DEFAULT NULL
     * 用于构建部门树形结构，null 表示顶级部门
     */
    private Integer parentId;

    /**
     * 部门状态
     * 数据库字段：status INT DEFAULT 1
     * 1-启用，0-禁用
     */
    private Integer status;

    /**
     * 排序号
     * 数据库字段：sort_order INT DEFAULT 0
     * 用于部门列表排序，数字越小越靠前
     */
    private Integer sortOrder;

    // ==================== 时间字段 ====================

    /**
     * 创建时间
     * 数据库字段：create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
     */
    private java.util.Date createTime;

    /**
     * 更新时间
     * 数据库字段：update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
     */
    private java.util.Date updateTime;
}