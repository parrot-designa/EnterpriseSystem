package com.enterprisesystem.babysecure.shiro.loginchain;

import com.enterprisesystem.babysecure.exception.ExceptionEnum;
import com.enterprisesystem.babysecure.model.entity.UserEntity;
import com.enterprisesystem.babysecure.shiro.entity.LoginErrorResult;

public class AccountChain extends AbstractLoginChain{
    public AccountChain(){
        this.nextChain = new PasswordChain();
    }

    @Override
    LoginErrorResult goChain(){
        UserEntity user = this.getUser();

        if(user == null){
            return LoginErrorResult.getLoginErrorResult(ExceptionEnum.ACCOUNT_LOGIN_ERR.getErrorCode(),ExceptionEnum.ACCOUNT_LOGIN_ERR.getErrMsg());
        }
        return null;
    }
}
