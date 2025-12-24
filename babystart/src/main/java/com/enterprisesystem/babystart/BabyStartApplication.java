package com.enterprisesystem.babystart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * Main application class for BabyStart
 * This is the entry point of the entire enterprise system
 */
@SpringBootApplication
@ComponentScan(
    basePackages = {
        "com.enterprisesystem.babystart",
        "com.enterprisesystem.babysecure"
    }
)
public class BabyStartApplication {

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("项目刚开始编译");
        System.out.println("===========================================");
        SpringApplication.run(BabyStartApplication.class, args);
        System.out.println("===========================================");
        System.out.println("项目编译成功! ");
        System.out.println("===========================================");
    }
}
