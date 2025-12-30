package com.enterprisesystem.babycommon.model;

import lombok.Data;

/**
 * 通用分页查询请求对象
 *
 * @author Claude Code
 */
@Data
public class PageRequest {

    /**
     * 当前页码（从1开始）
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方向（ASC/DESC）
     */
    private String sortOrder;

    /**
     * 构造函数
     */
    public PageRequest() {
    }

    /**
     * 构造函数
     *
     * @param page     当前页码
     * @param pageSize 每页大小
     */
    public PageRequest(Integer page, Integer pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    /**
     * 静态工厂方法
     *
     * @param page     当前页码
     * @param pageSize 每页大小
     * @return 分页请求对象
     */
    public static PageRequest of(Integer page, Integer pageSize) {
        return new PageRequest(page, pageSize);
    }

    /**
     * 计算偏移量
     *
     * @return 偏移量
     */
    public Integer getOffset() {
        if (page == null || page < 1) {
            return 0;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        return (page - 1) * pageSize;
    }

    /**
     * 验证分页参数
     */
    public void validate() {
        if (this.page == null || this.page < 1) {
            this.page = 1;
        }
        if (this.pageSize == null || this.pageSize < 1) {
            this.pageSize = 10;
        }
        if (this.pageSize > 500) {
            this.pageSize = 500; // 限制最大每页大小
        }
    }

}
