package com.common.config;

import com.common.redis.RedisDbDao;
import com.common.redis.RedisDbDaoImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
/**
 * redis
 *
 * @author shilun
 */
@Configuration
@Conditional(RedisCondition.class)
public class RedisConfig extends CachingConfigurerSupport {
    @Value("${spring.redis.url}")
    private String redisUrl;
    @Bean
    @RefreshScope
    public RedisDbDao newRedisDao(){
        RedisDbDaoImpl dao=new RedisDbDaoImpl();
        dao.setUse(true);
        redis.clients.jedis.JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxIdle(300);
        redis.clients.jedis.JedisShardInfo jedisShardInfo=new JedisShardInfo(redisUrl);
        ArrayList<JedisShardInfo> shards = new ArrayList<>();
        shards.add(jedisShardInfo);
        redis.clients.jedis.ShardedJedisPool shardedJedisPool=new ShardedJedisPool(config, shards);
        dao.setShardedJedisPool(shardedJedisPool);
        return dao;
    }

}