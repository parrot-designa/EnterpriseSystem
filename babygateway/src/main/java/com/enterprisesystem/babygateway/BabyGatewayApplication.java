package com.enterprisesystem.babygateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Baby Gateway 网关启动类
 *
 * @author Claude Code
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class BabyGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BabyGatewayApplication.class, args);
        System.out.println("Baby Gateway 启动成功！");
    }
}
