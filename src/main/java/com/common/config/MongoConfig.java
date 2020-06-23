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

import com.common.mongo.LongToMoneyConvert;
import com.common.mongo.MoneyToLongConvert;
import com.common.mongo.SaveMongoEventListener;
import com.common.util.StringUtils;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import org.bson.conversions.Bson;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.util.TypeInformation;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Mongo.
 */
@Configuration
@Conditional(MongodbCondition.class)
public class MongoConfig implements ApplicationContextAware, ResourceLoaderAware {

    private ApplicationContext applicationContext;
    private ResourceLoader resourceLoader;

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
        public MongoTransactionManager transactionManager() {
            return new MongoTransactionManager(dbFactory);
        }
    }

    @Resource
    private MongoDatabaseFactory dbFactory;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean("primary")
    public MongoTemplate mongoPrimaryTemplate() throws Exception {
        if (StringUtils.isBlank(mongodbUrl)) {
            throw new Exception("mongodb load error url" + mongodbUrl);
        }
        MongoTemplate mongoTemplate = new MongoTemplate(dbFactory);
        mongoTemplate.setReadPreference(ReadPreference.primary());
        mongoTemplate.setWriteConcern(WriteConcern.MAJORITY);
        return mongoTemplate;
    }

    @Bean("secondary")
    public MongoTemplate mongoSecondaryTemplate() throws Exception {
        if (StringUtils.isBlank(mongodbUrl)) {
            throw new Exception("mongodb load error url" + mongodbUrl);
        }
        MongoTemplate mongoTemplate = new MongoTemplate(dbFactory);
        mongoTemplate.setReadPreference(ReadPreference.secondary());
        mongoTemplate.setWriteConcern(WriteConcern.MAJORITY);
        return mongoTemplate;
    }

    @Bean
    public SaveMongoEventListener mongoEventListener() {
        return new SaveMongoEventListener();
    }


}


