package com.chippy.redis.example;

import com.chippy.redis.enhance.service.EnhanceObjectService;
import com.chippy.redis.example.po.TestBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 测试活性对象控制器
 *
 * @author: chippy
 **/
@Slf4j
@RestController
@RequestMapping("/test/enhance-object")
public class TestEnhanceObjectController {

    @Resource
    private EnhanceObjectService enhanceObjectService;

    @PostMapping("/creation")
    public TestBean creation(String id, String name) {
        final TestBean t = new TestBean();
        t.setId(id);
        t.setName(name);
        t.setAddress("测试地址");
        t.setAge(10);
        final HashMap beanMap = new HashMap();
        beanMap.put("1", "1");
        beanMap.put("2", "2");
        t.setBeanMap(beanMap);
        final TestBean.TestObject testObject = new TestBean.TestObject();
        testObject.setName("object name");
        t.setTestObject(testObject);
        enhanceObjectService.enhance(t);
        return t;
    }

    @PostMapping("/process")
    public String process(String id, int size) {
        for (int i = 0; i < size; i++) {
            final Thread t = new Thread(() -> {
                final TestBean testBean = enhanceObjectService.get(id, TestBean.class);
                testBean.increase("age", 1L);
            });
            t.start();
        }
        return "SUCCESS";
    }

    @PostMapping("/shutdown")
    public String shutdown(String id) {
        enhanceObjectService.shutdown(id);
        return "SUCCESS";
    }

}