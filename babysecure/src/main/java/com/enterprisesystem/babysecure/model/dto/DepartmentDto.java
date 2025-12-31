package com.enterprisesystem.babysecure.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门数据传输对象
 *
 * @Data 注解是 Lombok 提供的，自动生成以下方法：
 * - getter/setter：获取和设置字段值
 * - toString()：对象转字符串
 * - equals()：判断对象相等
 * - hashCode()：生成哈希值
 *
 * 这样就不用手动写这些重复代码了！
 */
@Data
public class DepartmentDto implements Serializable {

    // ==================== 基本信息 ====================

    /**
     * 部门ID
     * Integer 是 int 的包装类，可以为 null
     */
    private Integer id;

    /**
     * 部门编码
     * 例如：DEPT001, DEPT002
     */
    private String code;

    /**
     * 部门名称
     * 例如：技术部、市场部
     */
    private String name;

    // ==================== 层级结构 ====================

    /**
     * 父部门ID
     * 如果为 null，表示是顶级部门（根节点）
     * 如果有值，表示是某个部门的子部门
     */
    private Integer parentId;

    /**
     * 部门状态
     * 1 - 启用（正常）
     * 0 - 禁用（停用）
     */
    private Integer status;

    /**
     * 排序号
     * 用于部门列表的显示顺序，数字越小越靠前
     */
    private Integer sortOrder;

    // ==================== 时间字段 ====================

    /**
     * 创建时间
     * 记录部门何时创建
     */
    private Date createTime;

    /**
     * 更新时间
     * 记录部门最后一次修改的时间
     */
    private Date updateTime;

    // ==================== 扩展字段（可选）====================

    /**
     * 子部门列表
     * 这个字段在数据库中不存在
     * 用于前端展示树形结构时，包含该部门下的所有子部门
     */
    private java.util.List<DepartmentDto> children;
}
