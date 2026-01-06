package com.enterprisesystem.babycommon.dao;

import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisDao {
    private final StringRedisTemplate redisTemplate;
//    private final RedissonClient redissonClient;

    public RedisDao(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
//        this.redissonClient = redissonClient;
    }

    /**
     * 根据 key进行自增操作
     * @param key
     * @return
     */
    public long incr(String key){
        ValueOperations<String,String> ops = redisTemplate.opsForValue();
        Long value = ops.increment(key,1);
        return value;
    }
}
