/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.common.config;

import com.common.mongo.*;
import com.common.util.StringUtils;
import com.mongodb.*;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Mongo.
 */
@Configuration
@Conditional(MongodbCondition.class)
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongodbUrl;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @ConditionalOnProperty(name = "app.db.transaction")
    class MongoTransactionConfig {

        @Value("${app.db.transaction}")
        private Boolean transaction;
        @Bean
        public TransBean getTransBean() {
            TransBean transBean = new TransBean();
            transBean.setTransaction(transaction);
            return transBean;
        }
    }


    @Autowired(required = false)
    private TransBean transBean;

    @Bean
    @ConditionalOnMissingBean(MongoTemplate.class)
    public MongoTemplate mongoTemplate(MongoDbFactory dbFactory, MappingMongoConverter converter) throws Exception {
        if (StringUtils.isBlank(mongodbUrl)) {
            throw new Exception("mongodb load error url" + mongodbUrl);
        }
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        MongoTemplate mongoTemplate = new MongoTemplate(dbFactory, converter);
        if (transBean == null) {
            mongoTemplate.setReadPreference(ReadPreference.secondary());
            mongoTemplate.setWriteConcern(WriteConcern.W1);
        }
        if (transBean != null && transBean.getTransaction()) {
            mongoTemplate.setReadPreference(ReadPreference.primary());
            mongoTemplate.setWriteConcern(WriteConcern.MAJORITY);
        }
        return mongoTemplate;
    }

    @Bean
    public MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List list = new ArrayList();
        list.add(new MoneyToLongConvert());
        list.add(new IGlossaryToIntegerConvert());
        list.add(new LongToMoneyConvert());
        list.add(new BigDecimalToDecimal128Converter());
        list.add(new Decimal128ToBigDecimalConverter());
        return new MongoCustomConversions(list);
    }

    @Bean
    public MongoDbFactory dbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(mongo(), database);
    }

    @Resource
    private MongoMappingContext mongoMappingContext;

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoCustomConversions customConversions) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));//去掉默认mapper添加的_class
        mappingConverter.setCustomConversions(customConversions);//添加自定义的转换器

        return mappingConverter;
    }

    private MongoClient mongo;

    @PreDestroy
    public void close() {
        if (this.mongo != null) {
            this.mongo.close();
        }
    }

    @Bean
    public SaveMongoEventListener mongoEventListener() {
        return new SaveMongoEventListener();
    }

    @Bean
    public MongoClient mongo() throws UnknownHostException {
        if (StringUtils.isBlank(mongodbUrl)) {
            return null;
        }
        MongoClientURI uri = null;
        if (transBean != null && transBean.getTransaction()) {
            uri = new MongoClientURI(mongodbUrl, MongoClientOptions.builder().writeConcern(WriteConcern.MAJORITY).readPreference(ReadPreference.primary()));
        } else {
            uri = new MongoClientURI(mongodbUrl, MongoClientOptions.builder().writeConcern(WriteConcern.MAJORITY).readPreference(ReadPreference.secondary()));
        }
        mongo = new MongoClient(uri);

        return mongo;
    }

}