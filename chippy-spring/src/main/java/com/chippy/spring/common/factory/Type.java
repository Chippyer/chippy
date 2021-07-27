package com.chippy.spring.common.factory;

import java.util.List;

/**
 * 类型标志
 *
 * @author: chippy
 * @datetime 2021/7/5 22:51
 */
public interface Type {

    /**
     * 获取支持的类型信息
     *
     * @return java.util.List<java.lang.String>
     * @author chippy
     * @date 2021-07-27 11:34
     */
    List<String> getSupportTypeList();

}
