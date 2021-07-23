package com.chippy.user.controller;

import com.chippy.user.response.ResponseResult;
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
    public ResponseResult<String> helloworld(String hello) {
        return ResponseResult.success("hello world " + hello);
    }

}