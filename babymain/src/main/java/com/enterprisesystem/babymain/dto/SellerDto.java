package com.enterprisesystem.babymain.dto;

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

    public SellerDto() {
    }

    public SellerDto(Integer id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}
