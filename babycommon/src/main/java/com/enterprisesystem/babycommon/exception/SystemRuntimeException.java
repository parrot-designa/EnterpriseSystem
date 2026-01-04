package com.enterprisesystem.babycommon.exception;

public class SystemRuntimeException extends RuntimeException{
    private int errorCode = 298807723;

    // 源错误信息
    private String originErrorInfo;

    public SystemRuntimeException(int errorCode, String messgae){
        super(messgae);
        this.originErrorInfo = messgae;
        this.errorCode = errorCode;
    }
}
