package com.chippy.redis.example;

import com.chippy.redis.enhance.service.EnhanceObjectService;
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
            final Thread t1 = new Thread(() -> {
                final TestBean testBean = enhanceObjectService.get(id, TestBean.class);
                // final Integer age = testBean.getAge();
                //                System.out.println(String.format(Thread.currentThread().getName() + "start time-[%s]", System.currentTimeMillis()));
                //                log.debug(Thread.currentThread().getName() + "[{}]-before age-[{}]", Thread.currentThread().getName(),
                //                    age);
                //                testBean.setAge(age + 1);
                testBean.increase("age", 1L);

                //                final String s = UUIDUtil.generateUuid();
                //                System.out.println(s);
                //                testBean.setName(s);

                //                testBean.setAddress(UUIDUtil.generateUuid());
            });
            t1.setName("t1");
            t1.setPriority(1);
            final Thread t2 = new Thread(() -> {
                final TestBean testBean = enhanceObjectService.get(id, TestBean.class);
                // final Integer age = testBean.getAge();
                //                System.out.println(String.format(Thread.currentThread().getName() + "start time-[%s]", System.currentTimeMillis()));
                //                log.debug(Thread.currentThread().getName() + "[{}]-before age-[{}]", Thread.currentThread().getName(),
                //                    age);
                // testBean.setAge(age + 1);
                testBean.increase("age", 2L);

                //                testBean.setName(UUIDUtil.generateUuid());

                //                testBean.setAddress(UUIDUtil.generateUuid());
            });
            t2.setName("t2");
            t2.setPriority(3);
            final Thread t3 = new Thread(() -> {
                final TestBean testBean = enhanceObjectService.get(id, TestBean.class);
                // final Integer age = testBean.getAge();
                //                System.out.println(String.format(Thread.currentThread().getName() + "start time-[%s]", System.currentTimeMillis()));
                //                log.debug(Thread.currentThread().getName() + "[{}]-before age-[{}]", Thread.currentThread().getName(),
                //                    age);
                // testBean.setAge(age + 1);
                testBean.increase("age", 3L);

                //                testBean.setName(UUIDUtil.generateUuid());

                //                testBean.setAddress(UUIDUtil.generateUuid());
            });
            t3.setName("t3");
            t3.setPriority(7);

            t1.start();
            t2.start();
            t3.start();
        }
        return "SUCCESS";
    }

}