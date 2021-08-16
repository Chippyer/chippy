package com.chippy.redis.example;

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
    private RedisTemplateEnhanceObjectService redisTemplateEnhanceObjectService;

    @PostMapping("/creation")
    public TestBean creation(String id, String name) {
        final TestBean t = new TestBean();
        t.setId(id);
        t.setName(name);
        t.setAge(10);
        final HashMap beanMap = new HashMap();
        beanMap.put("1", "1");
        beanMap.put("2", "2");
        t.setBeanMap(beanMap);
        final TestBean.TestObject testObject = new TestBean.TestObject();
        testObject.setName("object name");
        t.setTestObject(testObject);
        redisTemplateEnhanceObjectService.enhance(t);
        return t;
    }

    @PostMapping("/process")
    public String process(String id, int size) {
        for (int i = 0; i < size; i++) {
            final Thread t1 = new Thread(() -> {
                final TestBean testBean = redisTemplateEnhanceObjectService.get(id, TestBean.class);
                final Integer age = testBean.getAge();
                log.debug(Thread.currentThread().getName() + "[{}]-before age-[{}]", Thread.currentThread().getName(),
                    age);
                testBean.setAge(age + 1);
                log.debug(Thread.currentThread().getName() + "[{}]-after age-[{}]", Thread.currentThread().getName(),
                    testBean.getAge());
            });
            t1.setName("t1");
            final Thread t2 = new Thread(() -> {
                final TestBean testBean = redisTemplateEnhanceObjectService.get(id, TestBean.class);
                final Integer age = testBean.getAge();
                log.debug(Thread.currentThread().getName() + "[{}]-before age-[{}]", Thread.currentThread().getName(),
                    age);
                testBean.setAge(age + 1);
                log.debug(Thread.currentThread().getName() + "[{}]-after age-[{}]", Thread.currentThread().getName(),
                    testBean.getAge());
            });
            t2.setName("t2");
            final Thread t3 = new Thread(() -> {
                final TestBean testBean = redisTemplateEnhanceObjectService.get(id, TestBean.class);
                final Integer age = testBean.getAge();
                log.debug(Thread.currentThread().getName() + "[{}]-before age-[{}]", Thread.currentThread().getName(),
                    age);
                testBean.setAge(age + 1);
                log.debug(Thread.currentThread().getName() + "[{}]-after age-[{}]", Thread.currentThread().getName(),
                    testBean.getAge());
            });
            t3.setName("t3");

            t1.start();
            t2.start();
            t3.start();
        }
        return "SUCCESS";
    }

}