package com.chippy.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @title: 订单应用程序
 * @author: chippy
 * @date: 2021-07-23 17:07
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.chippy.order.feign")
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}