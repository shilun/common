package com.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;


public class DistributedLockImpl implements DistributedLock {
    int LOCK_EXPIRE = 10 * 1000; //锁超时，防止线程在入锁以后，无限的执行等待
    private static Logger logger = LoggerFactory.getLogger(DistributedLockImpl.class);
    public static final String REDIS_LOCK = "RedisLock:";
    private RedisTemplate redisTemplate;
    private String key;

    public DistributedLockImpl(RedisTemplate redisTemplate, String key) {
        this.redisTemplate = redisTemplate;
        this.key = key;
    }

    public DistributedLockImpl(RedisTemplate redisTemplate, String key, int lockExpire) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.LOCK_EXPIRE = lockExpire;
    }

    @Override
    public synchronized boolean acquire() {
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {
            long expireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;
            Boolean acquire = connection.setNX(key.getBytes(), String.valueOf(expireAt).getBytes());
            if (acquire) {
                return true;
            } else {
                byte[] value = connection.get(key.getBytes());
                if (Objects.nonNull(value) && value.length > 0) {
                    long expireTime = Long.parseLong(new String(value));
                    if (expireTime < System.currentTimeMillis()) {
                        byte[] oldValue = connection.getSet(key.getBytes(), String.valueOf(System.currentTimeMillis() + LOCK_EXPIRE + 1).getBytes());
                        return Long.parseLong(new String(oldValue).replaceAll("\"","")) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

    @Override
    public synchronized void release() {
        redisTemplate.opsForValue().getOperations().delete(key);
    }

}
