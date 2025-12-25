package com.enterprisesystem.babycommon.entity;

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

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public int getApiId() {
        return apiId;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public APIResult() {
    }

    public APIResult(int errorCode, T data, String errorInfo, int apiId, boolean success) {
        this.errorCode = errorCode;
        this.data = data;
        this.errorInfo = errorInfo;
        this.apiId = apiId;
        this.success = success;
    }
}
