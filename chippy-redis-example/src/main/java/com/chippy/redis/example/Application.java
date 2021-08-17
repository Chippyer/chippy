package com.chippy.redis.example;

import com.chippy.redis.annotation.EnableEnhanceObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: chippy
 */
@SpringBootApplication
@EnableEnhanceObject
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
