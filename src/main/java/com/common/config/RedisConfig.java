package com.common.config;

import com.common.fastxml.MoneySerialize;
import com.common.redis.RedisDbDao;
import com.common.redis.RedisDbDaoImpl;
import com.common.redis.RedisObjectSerializer;
import com.common.redis.RedisUtil;
import com.common.util.Money;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
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

    @Resource
    private JedisShardInfo jedisShardInfo;
    @Bean
    @RefreshScope
    public RedisDbDao newRedisDao(){
        RedisDbDaoImpl dao=new RedisDbDaoImpl();
        dao.setUse(true);
        redis.clients.jedis.JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxIdle(300);
        ArrayList<JedisShardInfo> shards = new ArrayList<>();
        shards.add(jedisShardInfo);
        redis.clients.jedis.ShardedJedisPool shardedJedisPool=new ShardedJedisPool(config, shards);
        dao.setShardedJedisPool(shardedJedisPool);
        return dao;
    }
    @Bean
    @RefreshScope
    public JedisShardInfo buildJedisShardInfo(){
        return new JedisShardInfo(redisUrl);
    }
    @Bean
    @RefreshScope
    public JedisConnectionFactory buildJedisConnectionFactory(JedisShardInfo jedisShardInfo){
        return new JedisConnectionFactory(jedisShardInfo);
    }
    @Bean
    @RefreshScope
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    @RefreshScope
    public RedisUtil redisUtil(RedisTemplate redisTemplate){
        RedisUtil redisUtil=new RedisUtil(redisTemplate);
        return redisUtil;
    }

}