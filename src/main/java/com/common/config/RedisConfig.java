package com.common.config;

import com.common.redis.DistributedLockUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis
 *
 * @author shilun
 */
@Configuration
@Conditional(RedisCondition.class)
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
    @ConditionalOnProperty(name="spring.redis.url")
    class RedissonSingleConfig  {
        @Value("${spring.redis.url}")
        private String url;
        @Bean
        public RedissonClient getRedisson(){
            Config config = new Config();
            config.useSingleServer().setAddress(url);
            return Redisson.create(config);
        }
        @Bean
        public DistributedLockUtil distributedLockUtil() {
            return new DistributedLockUtil(getRedisson());
        }
    }

    @ConditionalOnProperty(name="spring.redis.cluster.nodes")
    class RedissonClientClusterConfig  {
        @Value("${spring.redis.cluster.nodes}")
        private String url;
        @Bean
        public RedissonClient getRedisson(){
            Config config = new Config();
            config.useClusterServers().addNodeAddress(url.split(","));
            return Redisson.create(config);
        }
        @Bean
        public DistributedLockUtil distributedLockUtil() {
            return new DistributedLockUtil(getRedisson());
        }
    }
    @ConditionalOnProperty(name="spring.redis.url")
    class RedissonSingleConfig  {
        @Value("${spring.redis.url}")
        private String url;
        @Bean
        public RedissonClient getRedisson(){
            Config config = new Config();
            config.useSingleServer().setAddress(url);
            return Redisson.create(config);
        }
        @Bean
        public DistributedLockUtil distributedLockUtil() {
            return new DistributedLockUtil(getRedisson());
        }
    }

    @ConditionalOnProperty(name="spring.redis.cluster.nodes")
    class RedissonClientClusterConfig  {
        @Value("${spring.redis.cluster.nodes}")
        private String url;
        @Bean
        public RedissonClient getRedisson(){
            Config config = new Config();
            String[] items = url.split(",");
            for(String item:items){
                config.useClusterServers().addNodeAddress("redis://"+ item);
            }
            return Redisson.create(config);
        }
        @Bean
        public DistributedLockUtil distributedLockUtil() {
            return new DistributedLockUtil(getRedisson());
        }
    }


}