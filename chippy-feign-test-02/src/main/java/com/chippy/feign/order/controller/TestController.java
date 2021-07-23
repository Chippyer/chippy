package com.chippy.feign.order.controller;

import com.chippy.feign.order.feign.TestFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.chippy.feign.order.response.ResponseResult;

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
        return ResponseResult.success(testFeignClient.helloworld().getData());
    }

}