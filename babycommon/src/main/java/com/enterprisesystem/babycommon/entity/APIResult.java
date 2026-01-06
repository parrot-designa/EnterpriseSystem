package com.enterprisesystem.babycommon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResult<T> {
    /**
     * 错误代码
     */
    private int errorCode;
    /**
     * 返回值
     */
    private T data;
    /**
     * 错误信息
     */
    private String errorInfo;
    /**
     * 接口id
     */
    private int apiId;
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 接口耗时（ms）
     */
    private double elapsed;

    /**
     * 单个参数构造器
     * @param data
     */
    public APIResult(T data){this.data = data;}
}
