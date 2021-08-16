package com.chippy.redis.example;

import cn.hutool.json.JSONUtil;
import com.chippy.redis.enhance.RedissonEnhanceObjectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 测试托管对象服务操作控制器
 *
 * @author: chippy
 **/
@RestController
@RequestMapping("/test/delegate-object-service")
public class TestDelegateObjectServiceController {

    @Resource
    private RedissonEnhanceObjectService redissonEnhanceObjectService;

    @GetMapping("/lo/set")
    public String setLoSet(String id, String name) {
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
        redissonEnhanceObjectService.enhance(t);
        return "SUCCESS";
    }

    @GetMapping("/lo/update")
    public String updateLoGet(String id, String name) {
        final TestBean testBean = redissonEnhanceObjectService.get(id, TestBean.class);
        testBean.setName(name);

        final HashMap beanMap = new HashMap();
        beanMap.put("1", "1");
        beanMap.put("3", "3");
        testBean.setBeanMap(beanMap);
        return testBean.getId() + "-" + testBean.getName();
    }

    @GetMapping("/lo/get")
    public String getLoGet(String id) {
        final TestBean testBean = redissonEnhanceObjectService.get(id, TestBean.class);
        return JSONUtil.toJsonStr(testBean);
    }

}