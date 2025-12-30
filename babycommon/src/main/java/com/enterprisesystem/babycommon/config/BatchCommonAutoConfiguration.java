package com.enterprisesystem.babycommon.config;

import com.enterprisesystem.babycommon.context.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchCommonAutoConfiguration {
    @Bean
    public ApplicationContextProvider applicationContextProvider(ApplicationContext context){
        return new ApplicationContextProvider();
    }
}
