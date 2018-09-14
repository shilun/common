package com.common.config;

import com.common.redis.DistributedLockUtil;
import com.common.redis.GenericFastJson2JsonRedisSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisShardInfo;

import javax.annotation.Resource;

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
    public JedisShardInfo buildJedisShardInfo() {
        return new JedisShardInfo(redisUrl);
    }

    @Bean
    public JedisConnectionFactory buildJedisConnectionFactory(JedisShardInfo jedisShardInfo) {
        return new JedisConnectionFactory(jedisShardInfo);
    }


    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new org.springframework.data.redis.serializer.StringRedisSerializer());
        template.setValueSerializer(new GenericFastJson2JsonRedisSerializer());
        template.setHashKeySerializer(new org.springframework.data.redis.serializer.StringRedisSerializer());
        template.setHashValueSerializer(new GenericFastJson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public DistributedLockUtil distributedLockUtil(RedisTemplate redisTemplate) {
        return new DistributedLockUtil();
    }
}