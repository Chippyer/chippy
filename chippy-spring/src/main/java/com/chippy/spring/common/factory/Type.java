package com.chippy.spring.common.factory;

import java.util.List;

/**
 * 类型标志
 *
 * @author: chippy
 */
public interface Type {

    /**
     * 获取支持的类型信息
     *
     * @return 标识支持的类型集合
     * @author chippy
     */
    List<String> getSupportTypeList();

}
