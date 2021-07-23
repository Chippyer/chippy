package com.chippy.feign.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @title: 测试FeignClient应用程序
 * @author: chippy
 * @date: 2021-07-23 17:07
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class TestFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestFeignApplication.class, args);
    }

}