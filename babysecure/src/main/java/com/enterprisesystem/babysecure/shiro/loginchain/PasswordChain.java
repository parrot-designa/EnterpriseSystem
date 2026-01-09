package com.enterprisesystem.babysecure.shiro.loginchain;

import com.alibaba.fastjson.JSONObject;
import com.enterprisesystem.babycommon.helpers.SHAHelper;
import com.enterprisesystem.babysecure.exception.ExceptionEnum;
import com.enterprisesystem.babysecure.model.entity.UserEntity;
import com.enterprisesystem.babysecure.shiro.config.JwtRealm;
import com.enterprisesystem.babysecure.shiro.entity.LoginErrorResult;
import com.enterprisesystem.babysecure.shiro.template.UserHandler;

public class PasswordChain extends AbstractLoginChain{

    public PasswordChain() {this.nextChain = null;}

    @Override
    LoginErrorResult goChain(){
        UserEntity user = this.getUser();
        JSONObject jsonObject = JwtRealm.params.get();
        String pwd = (String) jsonObject.get("pwd");
        String password = SHAHelper.convertByteToHexString(pwd);
        boolean checkResult = user.getPassword().equals(password);
        if(!checkResult){
            return LoginErrorResult.getLoginErrorResult(ExceptionEnum.PASSWORD_LOGIN_ERR.getErrorCode(), ExceptionEnum.PASSWORD_LOGIN_ERR.getErrMsg());
        }
        return null;
    }
}
