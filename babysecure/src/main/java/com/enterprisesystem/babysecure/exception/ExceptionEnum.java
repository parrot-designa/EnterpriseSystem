package com.enterprisesystem.babysecure.exception;

public enum ExceptionEnum {
    ACCOUNT_LOGIN_ERR(10001, "用户名错误！"),
    PASSWORD_LOGIN_ERR(10002, "密码错误！");

    private int errorCode;

    private String errMsg;

    ExceptionEnum(int code, String msg){
        this.errorCode = code;
        this.errMsg = msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrMsg(){
        return errMsg;
    }
}
