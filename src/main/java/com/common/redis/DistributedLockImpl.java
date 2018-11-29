package com.common.redis;

import com.common.exception.ApplicationException;
import com.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import java.util.Collections;


public class DistributedLockImpl implements DistributedLock {
    private static final Long RELEASE_SUCCESS = 1L;
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static Logger logger = LoggerFactory.getLogger(DistributedLockImpl.class);
    private JedisCommands jedis;
    private String key;
    private String requestId;
    int expireMsecs = 60 * 1000;
    private JedisLock jedisLock;

    public DistributedLockImpl(JedisCommands jedis, String key) {
        this.jedis = jedis;
        this.key = key;
        this.requestId = StringUtils.getUUID();
        if (jedis instanceof JedisCluster) {
            this.jedisLock = new JedisLock((JedisCluster) jedis, key.intern());
        }
    }

    public DistributedLockImpl(JedisCommands jedis, String key, int expireMsecs) {
        this.jedis = jedis;
        this.key = key;
        this.requestId = StringUtils.getUUID();
        if (jedis instanceof JedisCluster) {
            this.jedisLock = new JedisLock((JedisCluster) jedis, key.intern(), expireMsecs);
        }
    }

    public DistributedLockImpl(JedisCommands jedis, String key, int expireMsecs, int timeoutMsecs) {
        this.jedis = jedis;
        this.key = key;
        this.requestId = StringUtils.getUUID();
        if (jedis instanceof JedisCluster) {
            this.jedisLock = new JedisLock((JedisCluster) jedis, key.intern(), expireMsecs, timeoutMsecs);
        }
    }

    @Override
    public boolean acquire() {
        if (jedis instanceof JedisCluster) {
            return jedisLock.acquire();
        } else {
            String result = jedis.set(key, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireMsecs);
            if (LOCK_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        }
    }

    @Override
    public void release() {
        if (jedis instanceof JedisCluster) {
            jedisLock.release();
        }
        if (jedis instanceof Jedis) {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Jedis item = (Jedis) jedis;
            Object result = item.eval(script, Collections.singletonList(key), Collections.singletonList(requestId));
            if (!RELEASE_SUCCESS.equals(result)) {
                throw new ApplicationException("redis.unlock.error->key:" + key);
            }
        }

    }

}
