package com.common.redis;

import org.redisson.api.RedissonClient;

import javax.annotation.Resource;

public class DistributedLockUtil{
    @Resource
     private RedissonClient redissonClient;

    /**
     * 获取锁 默认 线程待等10 秒  锁 30秒
     * @param lockKey
     * @return
     */
    public  DistributedLock getDistributedLock(String lockKey){
        DistributedLock distributedLock=new DistributedLockImpl(redissonClient,lockKey);
        return distributedLock;
    }

    /**
     * 获取锁  锁 30秒
     * @param lockKey
     * @param waitTime 线程等待时间 线程等持时间
     * @return
     */
    public  DistributedLock getDistributedLock(String lockKey,int waitTime){
        DistributedLock distributedLock=new DistributedLockImpl(redissonClient,lockKey,waitTime);
        return distributedLock;
    }

    /**
     * 获取锁
     * @param key key
     * @param waitTime 线程等待时间
     * @param leaseTime 释放时间
     * @return
     */
    public DistributedLock getDistributedLock(String key, int waitTime, int leaseTime){
        DistributedLock distributedLock=new DistributedLockImpl(redissonClient,key,waitTime,leaseTime);
        return distributedLock;
    }

}