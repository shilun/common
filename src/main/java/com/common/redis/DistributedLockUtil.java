package com.common.redis;

import org.redisson.api.RedissonClient;

public class DistributedLockUtil {
    RedissonClient redissonClient;
    /**
     * 获取分布式锁
     * 默认获取锁10s超时，锁过期时间60s
     *
     * @return
     * @author yangwenkui
     * @time 2016年5月6日 下午1:30:46
     */
    public DistributedLock getDistributedLock(String lockKey) {
        DistributedLock distributedLock = new DistributedLockImpl(redissonClient, lockKey);
        return distributedLock;
    }

    public DistributedLock getDistributedLock(String lockKey, int expireMsecs) {
        DistributedLock distributedLock = new DistributedLockImpl(redissonClient, lockKey, expireMsecs);
        return distributedLock;
    }

    public DistributedLockUtil(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
}