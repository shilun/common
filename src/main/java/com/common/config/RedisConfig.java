package com.common.config;

import com.common.redis.DistributedLockUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

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
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);





//         <property name="keySerializer">
//                4             <bean
        ObjectMapper om = new ObjectMapper();
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(om);


        redisTemplate.setKeySerializer(serializer);
//        5                 class="org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer" />
//                6         </property>
//                7         <property name="valueSerializer">
//                8             <bean
//        9                 class="org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer" />
//                10         </property>
        redisTemplate.setValueSerializer(serializer);
//                11
//        12         <property name="hashKeySerializer">
//                13             <bean
//        14                 class="org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer" />
//                15         </property>
        redisTemplate.setHashKeySerializer(serializer);
//                16         <property name="hashValueSerializer">
//                17             <bean
//        18                 class="org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer" />
//                19         </property>
        redisTemplate.setHashValueSerializer(serializer);
//                20         <property name="stringSerializer">
//                21             <bean
//        22                 class="org.springframework.data.redis.serializer.StringRedisSerializer" />
//                23         </property>
        redisTemplate.setStringSerializer(new org.springframework.data.redis.serializer.StringRedisSerializer());
        return redisTemplate;
    }

//    @Bean
//    public DistributedLockUtil distributedLockUtil(RedisTemplate redisTemplate) {
//        return new DistributedLockUtil();
//    }
}