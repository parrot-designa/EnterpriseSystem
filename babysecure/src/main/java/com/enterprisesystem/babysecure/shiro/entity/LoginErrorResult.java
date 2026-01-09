package com.enterprisesystem.babysecure.shiro.entity;

import lombok.Data;

@Data
public class LoginErrorResult {
    /**
     * 错误码
     */
    Integer errorCode;

    /**
     *  错误信息
     */
    String errorMsg;

    public static LoginErrorResult getLoginErrorResult(Integer errorCode,String errorMsg){
        LoginErrorResult result = new LoginErrorResult();
        result.setErrorCode(errorCode);
        result.setErrorMsg(errorMsg);
        return result;
    }
}
