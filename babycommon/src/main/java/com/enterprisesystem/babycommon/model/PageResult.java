package com.enterprisesystem.babycommon.model;

import lombok.Data;

import java.util.List;

/**
 * 通用分页结果对象
 *
 * @param <T> 数据类型
 * @author Claude Code
 */
@Data
public class PageResult<T> {

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 构造函数
     */
    public PageResult() {
    }

    /**
     * 构造函数
     *
     * @param page     当前页码
     * @param pageSize 每页大小
     * @param total    总记录数
     * @param records  数据列表
     */
    public PageResult(Integer page, Integer pageSize, Long total, List<T> records) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.records = records;
        this.totalPages = calculateTotalPages(total, pageSize);
    }

    /**
     * 静态工厂方法
     *
     * @param page     当前页码
     * @param pageSize 每页大小
     * @param total    总记录数
     * @param records  数据列表
     * @param <T>      数据类型
     * @return 分页结果对象
     */
    public static <T> PageResult<T> of(Integer page, Integer pageSize, Long total, List<T> records) {
        return new PageResult<>(page, pageSize, total, records);
    }

    /**
     * 计算总页数
     *
     * @param total    总记录数
     * @param pageSize 每页大小
     * @return 总页数
     */
    private Integer calculateTotalPages(Long total, Integer pageSize) {
        if (total == null || total == 0 || pageSize == null || pageSize == 0) {
            return 0;
        }
        return (int) Math.ceil((double) total / pageSize);
    }

    /**
     * 是否有下一页
     *
     * @return true-有下一页，false-无下一页
     */
    public boolean hasNext() {
        return page != null && totalPages != null && page < totalPages;
    }

    /**
     * 是否有上一页
     *
     * @return true-有上一页，false-无上一页
     */
    public boolean hasPrevious() {
        return page != null && page > 1;
    }

    /**
     * 是否为第一页
     *
     * @return true-第一页，false-非第一页
     */
    public boolean isFirst() {
        return page != null && page == 1;
    }

    /**
     * 是否为最后一页
     *
     * @return true-最后一页，false-非最后一页
     */
    public boolean isLast() {
        return page != null && totalPages != null && page.equals(totalPages);
    }

}
