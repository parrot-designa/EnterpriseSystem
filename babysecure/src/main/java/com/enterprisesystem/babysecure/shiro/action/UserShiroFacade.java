package com.enterprisesystem.babysecure.shiro.action;

import com.enterprisesystem.babycommon.utils.CommonStringUtil;
import com.enterprisesystem.babysecure.shiro.config.Const;
import com.enterprisesystem.babysecure.shiro.request.LoginRequest;
import com.enterprisesystem.babysecure.shiro.template.AbstractLoginProcesserTemplate;
import com.enterprisesystem.babysecure.shiro.template.HandlerFactory;
import com.enterprisesystem.babysecure.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserShiroFacade {

    private String loginCheckBusiness = Const.LOCK_CHECK_BUSINESS;

    public Map<String,Object> doLogin(LoginRequest loginRequest) {
        // 前端传过来的是加密的 需解密
        // 前后端约定账号密码必填 使用 window.btoa进行加密
        String password = PasswordUtil.decrypt(loginRequest.getPassword());
        loginRequest.setPassword(password);

        Map<String,Object> stringObjectMap = new HashMap<>();
        String[] buss = loginCheckBusiness.split(Const.THE_COMMA);
        for(String name : buss){
            AbstractLoginProcesserTemplate handler = HandlerFactory.getHandler(name);
            assert handler !=null;
            handler.process(name, stringObjectMap, loginRequest);
        }
        return stringObjectMap;
    }


}
