package com.common.redis;

import com.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;


public class DistributedLockImpl implements DistributedLock, AutoCloseable {
    int expireMsecs = 10 * 1000; //锁超时，防止线程在入锁以后，无限的执行等待
    int timeoutMsecs = 10 * 1000; //锁等待，防止线程饥饿
    private static Logger logger = LoggerFactory.getLogger(DistributedLockImpl.class);
    public static final String REDIS_LOCK = "RedisLock:";
    private RedisTemplate redisTemplate;
    private String key;

    public DistributedLockImpl(RedisTemplate redisTemplate, String key) {
        this.redisTemplate = redisTemplate;
        this.key = key;
    }

    public DistributedLockImpl(RedisTemplate redisTemplate, String key, int expireMsecs) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.expireMsecs = expireMsecs;
    }

    public DistributedLockImpl(RedisTemplate redisTemplate, String key, int timeoutMsecs, int expireMsecs) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.timeoutMsecs = timeoutMsecs;
        this.expireMsecs = expireMsecs;
    }

    @Override
    public synchronized boolean acquire() {
        try {
            String content = (String) redisTemplate.opsForValue().getAndSet(generateLockKey(), "ok");
            if (StringUtils.isEmpty(content)) {
                redisTemplate.expire(generateLockKey(), expireMsecs, TimeUnit.MILLISECONDS);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("加锁失败", e);
            release();
        }
        return false;
    }

    @Override
    public synchronized void release() {
        redisTemplate.opsForValue().getOperations().delete(generateLockKey());
    }

    private String generateLockKey() {
        return String.format(REDIS_LOCK + "%s", key);
    }

    @Override
    public void close() throws Exception {
        redisTemplate.opsForValue().getOperations().delete(generateLockKey());
    }
}
