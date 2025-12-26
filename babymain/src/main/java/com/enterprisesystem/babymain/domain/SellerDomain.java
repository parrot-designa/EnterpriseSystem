package com.enterprisesystem.babymain.domain;

public class SellerDomain {
    private Integer id;
    private String code;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SellerDomain() {
    }

    public SellerDomain(Integer id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}
