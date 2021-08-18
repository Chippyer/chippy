package com.chippy.redis.example.po;

import com.chippy.redis.enhance.EnhanceObject;
import com.chippy.redis.enhance.FieldLock;
import lombok.Data;

/**
 * 库存实体
 *
 * @author: chippy
 */
@Data
public class Inventory implements EnhanceObject {

    private String id;

    private String name;

    @FieldLock
    private Long remainStock;

    @FieldLock
    private Long lockStock;

}
