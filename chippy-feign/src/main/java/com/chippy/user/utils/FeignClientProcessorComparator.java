package com.chippy.user.utils;

import com.chippy.user.support.processor.FeignClientProcessor;
import org.springframework.core.annotation.Order;

import java.util.Comparator;

/**
 * @title: FeignClient增强处理器比较器
 * @author: chippy
 * @date: 2021-07-23 16:07
 **/
public class FeignClientProcessorComparator implements Comparator<FeignClientProcessor> {

    @Override
    public int compare(FeignClientProcessor fcp1, FeignClientProcessor fcp2) {
        final Order o1 = fcp1.getClass().getAnnotation(Order.class);
        final Order o2 = fcp2.getClass().getAnnotation(Order.class);
        int o1Value = Integer.MAX_VALUE, o2Value = Integer.MAX_VALUE;
        if (null != o1) {
            o1Value = o1.value();
        }
        if (null != o2) {
            o2Value = o2.value();
        }
        return o2Value - o1Value;
    }

}