package com.chippy.redis.example.po;

import com.chippy.redis.enhance.EnhanceObject;
import com.chippy.redis.enhance.FieldLock;
import lombok.Data;

import java.util.Map;

/**
 * @author: chippy
 **/
@Data
public class TestBean implements EnhanceObject {

    private String id;

    @FieldLock
    private String name;

    private String address;

    @FieldLock
    private Integer age;

    private Map<String, String> beanMap;

    private TestObject testObject;

    @Data
    public static class TestObject {

        private String name;

    }

}