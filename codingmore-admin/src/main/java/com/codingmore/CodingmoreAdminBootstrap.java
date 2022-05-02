package com.codingmore;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * 后台管理系统接口主类
 */
@ServletComponentScan
@SpringBootApplication
public class CodingmoreAdminBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(CodingmoreAdminBootstrap.class);
    }
}

