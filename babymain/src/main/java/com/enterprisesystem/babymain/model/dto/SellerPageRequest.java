package com.enterprisesystem.babymain.model.dto;

import lombok.Data;

@Data
public class SellerPageRequest {
    /**
     * 当前页码（从1开始）
     */
    private Integer pageNum = 1;

    /**
     * 每页显示条数
     */
    private Integer pageSize = 10;

    /**
     * 商家代码（模糊查询）
     */
    private String code;

    /**
     * 商家名称（模糊查询）
     */
    private String name;

    /**
     * 获取分页起始位置
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
