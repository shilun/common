package com.common.config;

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
    public JedisShardInfo buildJedisShardInfo(){
        return new JedisShardInfo(redisUrl);
    }
    @Bean
    public JedisConnectionFactory buildJedisConnectionFactory(JedisShardInfo jedisShardInfo){
        return new JedisConnectionFactory(jedisShardInfo);
    }
    @Bean
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

}