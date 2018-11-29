package com.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import javax.annotation.Resource;

public class DistributedLockUtil {
    @Autowired(required = false)
    private Jedis jedis;

    @Autowired(required = false)
    private JedisCluster jedisCluster;

    /**
     * 获取分布式锁
     * 默认获取锁10s超时，锁过期时间60s
     *
     * @return
     * @author yangwenkui
     * @time 2016年5月6日 下午1:30:46
     */
    public DistributedLock getDistributedLock(String lockKey) {
        JedisCommands command = null;
        if (jedis != null) {
            command = jedis;
        }
        if (jedisCluster != null) {
            command = jedisCluster;
        }

        DistributedLock distributedLock = new DistributedLockImpl(command, lockKey);
        return distributedLock;
    }

    public DistributedLock getDistributedLock(String lockKey, int expireMsecs) {
        JedisCommands command = null;
        if (jedis != null) {
            command = jedis;
        }
        if (jedisCluster != null) {
            command = jedisCluster;
        }
        DistributedLock distributedLock = new DistributedLockImpl(command, lockKey, expireMsecs);
        return distributedLock;
    }

    /**
     * @param lockKey
     * @param timeoutMsecs
     * @param expireMsecs
     * @return
     */
    public DistributedLock getDistributedLock(String lockKey, int expireMsecs, int timeoutMsecs) {
        JedisCommands command = null;
        if (jedis != null) {
            command = jedis;
        }
        if (jedisCluster != null) {
            command = jedisCluster;
        }
        DistributedLock distributedLock = new DistributedLockImpl(command, lockKey, expireMsecs, timeoutMsecs);
        return distributedLock;
    }

}