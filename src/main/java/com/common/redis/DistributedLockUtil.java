package com.common.redis;

import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

public class DistributedLockUtil{
    @Resource
     private RedisTemplate redisTemplate;
    /**
     * 获取分布式锁
     * 默认获取锁10s超时，锁过期时间60s
     * @author yangwenkui
     * @time 2016年5月6日 下午1:30:46
     * @return
     */
    public  DistributedLock getDistributedLock(String lockKey){
        DistributedLock distributedLock=new DistributedLockImpl(redisTemplate,lockKey);
        return distributedLock;
    }

    public  DistributedLock getDistributedLock(String lockKey,int expireMsecs){
        DistributedLock distributedLock=new DistributedLockImpl(redisTemplate,lockKey,expireMsecs);
        return distributedLock;
    }

    /**
     *
     * @param lockKey
     * @param timeoutMsecs
     * @param expireMsecs
     * @return
     */
    public  DistributedLock getDistributedLock(String lockKey,int timeoutMsecs,int expireMsecs){
        DistributedLock distributedLock=new DistributedLockImpl(redisTemplate,lockKey,expireMsecs);
        return distributedLock;
    }

}