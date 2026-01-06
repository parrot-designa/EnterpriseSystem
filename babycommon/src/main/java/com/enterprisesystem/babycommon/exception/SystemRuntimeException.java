package com.enterprisesystem.babycommon.exception;

import lombok.Data;

@Data
public class SystemRuntimeException extends RuntimeException{
    private int errorCode = 298807723;

    // 源错误信息
    private String originErrorInfo;

    private Integer errorType = 0;

    public SystemRuntimeException(String messgae){
        super(messgae);
        this.originErrorInfo = messgae;
    }

    public SystemRuntimeException(int errorCode, String messgae){
        super(messgae);
        this.originErrorInfo = messgae;
        this.setErrorCode(errorCode);
    }

    public String getOrignMessage(){
        return super.getMessage();
    }
}
