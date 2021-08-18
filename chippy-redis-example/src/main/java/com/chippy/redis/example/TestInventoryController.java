package com.chippy.redis.example;

import cn.hutool.json.JSONUtil;
import com.chippy.redis.enhance.service.EnhanceObjectService;
import com.chippy.redis.example.po.Inventory;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 测试简易库存操作
 *
 * @author: chippy
 */
@RestController
@RequestMapping("/test/inventory")
public class TestInventoryController {

    @Resource
    private EnhanceObjectService enhanceObjectService;

    @Resource
    private RedissonClient redissonClient;

    @PostMapping("/creation")
    public String creation(String id, String name, long remainStock) {
        final Inventory inventory = new Inventory();
        inventory.setId(id);
        inventory.setName(name);
        inventory.setRemainStock(remainStock);
        inventory.setLockStock(0L);
        enhanceObjectService.enhance(inventory);
        return JSONUtil.toJsonStr(inventory);
    }

    @GetMapping("/get")
    public String get(String id) {
        return JSONUtil.toJsonStr(enhanceObjectService.get(id, Inventory.class));
    }

    @GetMapping("/getRemainStock")
    public Long getRemainStock(String id) {
        final RLock lock = redissonClient.getLock("lock:" + id);
        try {
            lock.tryLock(3000, 5000, TimeUnit.MILLISECONDS);
            final Inventory inventory = enhanceObjectService.get(id, Inventory.class);
            return inventory.getRemainStock() - inventory.getLockStock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(lock) && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return -1L;
    }

    @PostMapping("/lock")
    public String lock(String id, long lockStock) {
        final Inventory inventory = enhanceObjectService.get(id, Inventory.class);
        if (this.getRemainStock(id) < lockStock) {
            return "请稍后重试（库存不足）";
        }
        inventory.increase("lockStock", lockStock);
        return "抢购成功";
    }

    @PostMapping("/concurrentLock")
    public String concurrent(String id, int size) {
        for (int i = 0; i < size; i++) {
            final Thread t = new Thread(() -> {
                final String lockResult = lock(id, 5);
                System.out.println(String.format("[%s]-lock result-[%s]", Thread.currentThread().getId(), lockResult));
            });
            t.start();
        }
        return "SUCCESS";
    }

}
