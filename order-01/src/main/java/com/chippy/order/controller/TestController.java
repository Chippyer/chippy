package com.chippy.order.controller;

import com.chippy.order.feign.TestFeignClient;
import com.chippy.order.response.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @title: 测试控制器
 * @author: chippy
 * @date: 2021-07-23 17:07
 **/
@RestController
@RequestMapping("/test/feign")
public class TestController {

    @Resource
    private TestFeignClient testFeignClient;

    @GetMapping("/helloworld")
    public ResponseResult<String> helloworld() {
        return ResponseResult.success(testFeignClient.helloworld("test").getData());
    }

}