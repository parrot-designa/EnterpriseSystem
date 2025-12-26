package com.enterprisesystem.babymain.model.dto;

import lombok.Data;

@Data
public class SellerDto {
    /**
     * 商家id
     */
    private Integer id;
    /**
     * 商家代码
     */
    private String code;
    /**
     * 商家名称
     */
    private String name;
}
