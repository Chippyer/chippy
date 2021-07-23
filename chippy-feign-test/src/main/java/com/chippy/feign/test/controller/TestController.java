package com.chippy.feign.test.controller;

import com.chippy.feign.test.response.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: 测试控制器
 * @author: chippy
 * @date: 2021-07-23 17:07
 **/
@RestController
@RequestMapping("/test/feign")
public class TestController {

    @GetMapping("/helloworld")
    public ResponseResult<String> helloworld() {
        return ResponseResult.success("hello world");
    }

}