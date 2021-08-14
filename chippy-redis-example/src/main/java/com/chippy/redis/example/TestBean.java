package com.chippy.redis.example;

import com.chippy.redis.enhance.EnhanceObject;
import lombok.Data;

import java.util.Map;

/**
 * @author: chippy
 **/
@Data
public class TestBean implements EnhanceObject {

    private String id;

    private String name;

    private Integer age;

    private Map<String, String> beanMap;

    private TestObject testObject;

    @Data
    public static class TestObject {

        private String name;

    }

}