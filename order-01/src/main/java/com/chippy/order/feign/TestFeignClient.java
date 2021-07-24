package com.chippy.order.feign;

import com.chippy.feign.annotation.EnhanceRequest;
import com.chippy.order.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @title: 测试FeignClient
 * @author: chippy
 * @date: 2021-07-23 17:07
 **/
@EnhanceRequest
@FeignClient(value = "USER-SERVICE")
public interface TestFeignClient {

    @GetMapping("/test/feign/helloworld")
    ResponseResult<String> helloworld(@RequestParam("hello") String hello);

}