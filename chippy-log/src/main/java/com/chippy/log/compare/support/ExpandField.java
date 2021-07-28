package com.chippy.log.compare.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * 扩展属性对象
 *
 * @author: chippy
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpandField implements Serializable {

    /**
     * 属性对象
     */
    private Field field;

    /**
     * 操作描述
     */
    private String operateDesc;

}