package com.chippy.order.feign;

import com.chippy.order.response.ResponseResult;
import com.chippy.user.annotation.EnhanceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @title: 测试FeignClient
 * @author: chippy
 * @date: 2021-07-23 17:07
 **/
@FeignClient(value = "USER-SERVICE")
public interface TestFeignClient {

    @EnhanceRequest
    @GetMapping("/test/feign/helloworld")
    ResponseResult<String> helloworld(@RequestParam("hello") String hello);

}