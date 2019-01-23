package com.common.redis;

import com.common.exception.ApplicationException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class DistributedLockImpl implements DistributedLock {
    /**
     * 等待时间
     */
    private long waitTime = 10 * 1000;
    /**
     * 超时时间
     */
    private long leaseTime = 30 * 1000;

    private static Logger logger = LoggerFactory.getLogger(DistributedLockImpl.class);
    private RedissonClient redissonClient;
    private RLock lock;

    public DistributedLockImpl(RedissonClient redissonClient, String key) {
        this.redissonClient = redissonClient;
        this.lock = redissonClient.getFairLock(key);

    }

    public DistributedLockImpl(RedissonClient redissonClient, String key, int waitTime) {
        this.redissonClient = redissonClient;
        this.lock = redissonClient.getFairLock(key);
        this.waitTime = waitTime;
    }

    public DistributedLockImpl(RedissonClient redissonClient, String key, int waitTime, int leaseTime) {
        this.redissonClient = redissonClient;
        this.lock = redissonClient.getFairLock(key);
        this.waitTime = waitTime;
        this.leaseTime = leaseTime;
    }

    @Override
    public boolean acquire() {
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new ApplicationException("redis.lock.error", e);
        }
    }

    @Override
    public synchronized void release() {
        try {
            this.lock.forceUnlock();
        }
        catch (Exception e){
            logger.error("redis-lock.release.error",e);
        }
    }

}
