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

import com.common.exception.ApplicationException;
import com.common.mongo.*;
import com.common.util.StringUtils;
import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
        @Bean
        public MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
            return new MongoTransactionManager(dbFactory);
        }
    }


    @Autowired(required = false)
    private TransBean transBean;

    @Bean
    @ConditionalOnMissingBean(MongoTemplate.class)
    public MongoTemplate mongoTemplate(MongoDbFactory dbFactory) throws Exception {
        if (StringUtils.isBlank(mongodbUrl)) {
            throw new Exception("mongodb load error url" + mongodbUrl);
        }
        MongoTemplate mongoTemplate = new MongoTemplate(dbFactory, mappingMongoConverter(dbFactory));
        if (transBean == null) {
            mongoTemplate.setReadPreference(ReadPreference.primary());
            mongoTemplate.setWriteConcern(WriteConcern.MAJORITY);
        }
        if (transBean != null && transBean.getTransaction()) {
            mongoTemplate.setReadPreference(ReadPreference.primary());
            mongoTemplate.setWriteConcern(WriteConcern.MAJORITY);
        }
        return mongoTemplate;
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
        return new SimpleMongoDbFactory(mongo(), mongoClientURI().getDatabase());
    }

    @Resource
    private MongoMappingContext mongoMappingContext;

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));//去掉默认mapper添加的_class
        mappingConverter.setCustomConversions(customConversions());//添加自定义的转换器

        return mappingConverter;
    }
    private MongoClient mongo;
    private MongoClientURI mongoClientURI;

    @PreDestroy
    public void close() {
        if (this.mongo != null) {
            this.mongo.close();
        }
    }

    @Bean
    public MongoClientURI mongoClientURI() {
        if (this.mongoClientURI != null) {
            return this.mongoClientURI;
        }
        if (StringUtils.isBlank(mongodbUrl)) {
            throw new ApplicationException("mongodb load error url" + mongodbUrl);
        }
        com.mongodb.MongoClientURI url = new MongoClientURI(mongodbUrl, MongoClientOptions.builder().writeConcern(WriteConcern.MAJORITY).readPreference(ReadPreference.primary()));
        this.mongoClientURI = url;
        return url;
    }

    @Bean
    public SaveMongoEventListener mongoEventListener() {
        return new SaveMongoEventListener();
    }

    @Bean
    public MongoClient mongo() {
        if (this.mongo != null) {
            return this.mongo;
        }
        if (StringUtils.isBlank(mongodbUrl)) {
            return null;
        }
        MongoClient mongo = new MongoClient(mongoClientURI());
        this.mongo = mongo;
        return mongo;
    }

}