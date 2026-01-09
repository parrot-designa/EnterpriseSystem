package com.enterprisesystem.babysecure;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main application class for BabyStart
 * This is the entry point of the entire enterprise systems
 */
@SpringBootApplication
@ComponentScan(
        basePackages = {
                "com.enterprisesystem"
        }
)
@MapperScan({
        "com.enterprisesystem.babysecure.mapper"
})
public class BabySecureApplication {
    public static void main(String[] args) {
        SpringApplication.run(BabySecureApplication.class, args);
    }
}
