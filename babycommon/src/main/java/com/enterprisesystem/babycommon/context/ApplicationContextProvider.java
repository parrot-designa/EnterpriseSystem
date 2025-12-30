package com.enterprisesystem.babycommon.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        ApplicationContextProvider.applicationContext = applicationContext;
    }

    public static boolean isInit(){
        return applicationContext != null;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static <T> T getBean(Class<T> beanType){
        return getApplicationContext().getBean(beanType);
    }
}
