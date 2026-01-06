package com.enterprisesystem.babysecure.shiro.template;

import com.enterprisesystem.babysecure.shiro.request.LoginRequest;

import java.util.Map;

public abstract class AbstractLoginProcesserTemplate {

    public void process(String businessName,Map<String,Object> resultMap,LoginRequest loginRequest){
        Map<String,Object> map = this.doProcess(businessName, resultMap, loginRequest);
    }

    abstract Map<String,Object> doProcess(String businessName, Map<String,Object> resultMap, LoginRequest request);
}
