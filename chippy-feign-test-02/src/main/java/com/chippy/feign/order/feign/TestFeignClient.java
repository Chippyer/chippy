package com.chippy.feign.order.feign;

import com.chippy.feign.annotation.EnhanceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.chippy.feign.order.response.ResponseResult;

/**
 * @title: 测试FeignClient
 * @author: chippy
 * @date: 2021-07-23 17:07
 **/
@FeignClient(value = "USER-SERVICE")
public interface TestFeignClient {

    @EnhanceRequest
    @GetMapping("/test/feign/helloworld")
    ResponseResult<String> helloworld();

}