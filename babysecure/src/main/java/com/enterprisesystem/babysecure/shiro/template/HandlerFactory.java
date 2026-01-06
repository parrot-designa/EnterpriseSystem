package com.enterprisesystem.babysecure.shiro.template;

import com.enterprisesystem.babycommon.context.ApplicationContextProvider;
import com.enterprisesystem.babycommon.exception.SystemRuntimeException;

public class HandlerFactory {

    private static final String LOCK_CHECK_BUSINESS_USER = "User";

    public static AbstractLoginProcesserTemplate getHandler(String bussinessName){
        switch (bussinessName){
            case LOCK_CHECK_BUSINESS_USER:
                return ApplicationContextProvider.getBean(UserHandler.class);
            default:
                throw new SystemRuntimeException("");
        }
    }
}
