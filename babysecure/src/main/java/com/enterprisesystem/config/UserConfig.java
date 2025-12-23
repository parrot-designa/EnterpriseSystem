package com.enterprisesystem.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * User module configuration
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.enterprisesystem.controller",
    "com.enterprisesystem.service",
    "com.enterprisesystem.repository",
    "com.enterprisesystem.model",
    "com.enterprisesystem.api"
})
public class UserConfig {
}
