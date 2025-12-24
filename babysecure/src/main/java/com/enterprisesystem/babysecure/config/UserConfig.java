package com.enterprisesystem.babysecure.config;

import org.springframework.context.annotation.ComponentScan;

/**
 * User module configuration
 */
@ComponentScan(basePackages = {
    "com.enterprisesystem.babysecure.controller",
    "com.enterprisesystem.babysecure.service",
    "com.enterprisesystem.babysecure.repository",
    "com.enterprisesystem.babysecure.model",
    "com.enterprisesystem.babysecure.api"
})
public class UserConfig {
}
