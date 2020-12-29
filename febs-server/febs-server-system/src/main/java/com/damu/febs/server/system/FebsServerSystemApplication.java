package com.damu.febs.server.system;

import com.damu.febs.common.annotation.EnableFebsAuthExceptionHandler;
import com.damu.febs.common.annotation.EnableFebsServerProtect;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@EnableDiscoveryClient
@SpringBootApplication
@EnableTransactionManagement
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableFebsServerProtect
@EnableFebsAuthExceptionHandler
@MapperScan("com.damu.febs.server.system.mapper")
public class FebsServerSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FebsServerSystemApplication.class, args);
    }
}