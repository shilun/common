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
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import org.bson.conversions.Bson;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.util.TypeInformation;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
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
        public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
            return new MongoTransactionManager(dbFactory);
        }
    }


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean("primary")
    public MongoTemplate mongoPrimaryTemplate(MongoDatabaseFactory dbFactory) throws Exception {
        if (StringUtils.isBlank(mongodbUrl)) {
            throw new Exception("mongodb load error url" + mongodbUrl);
        }
        MongoTemplate mongoTemplate = new MongoTemplate(dbFactory, mappingMongoConverter(dbFactory, this.mongoMappingContext));
        mongoTemplate.setReadPreference(ReadPreference.primary());
        mongoTemplate.setWriteConcern(WriteConcern.MAJORITY);
        return mongoTemplate;
    }

    @Bean("secondary")
    public MongoTemplate mongoSecondaryTemplate(MongoDatabaseFactory dbFactory) throws Exception {
        if (StringUtils.isBlank(mongodbUrl)) {
            throw new Exception("mongodb load error url" + mongodbUrl);
        }
        MongoTemplate mongoTemplate = new MongoTemplate(dbFactory, mappingMongoConverter(dbFactory, this.mongoMappingContext));
        mongoTemplate.setReadPreference(ReadPreference.secondary());
        mongoTemplate.setWriteConcern(WriteConcern.MAJORITY);
        return mongoTemplate;
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter> list = new ArrayList();
        list.add(new MoneyToLongConvert());
        list.add(new LongToMoneyConvert());
        list.add(new BigDecimalToDecimal128Converter());
        list.add(new Decimal128ToBigDecimalConverter());
        return new MongoCustomConversions(list);
    }

    @Bean
    public SimpleMongoClientDatabaseFactory dbFactory()  {
        return new SimpleMongoClientDatabaseFactory(mongodbUrl);
    }

    @Resource
    private MongoMappingContext mongoMappingContext;

    @Bean
    @ConditionalOnMissingBean
    public MongoMappingContext mongoMappingContext(BeanFactory beanFactory, ApplicationContext applicationContext) throws ClassNotFoundException {
        MongoMappingContext context = new MongoMappingContext() {
            @Override
            public Collection<BasicMongoPersistentEntity<?>> getPersistentEntities() {
                return super.getPersistentEntities();
            }
        };

        return context;
    }


    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory factory, MongoMappingContext mongoMappingContext) {
        mongoMappingContext.setAutoIndexCreation(true);
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext){
        };

        DefaultMongoTypeMapper typeMapper = new DefaultMongoTypeMapper(null){
            @Override
            public <T> TypeInformation<? extends T> readType(Bson source, TypeInformation<T> basicType) {
                return super.readType(source, basicType);
            }

            @Override
            public TypeInformation<?> readType(Bson source) {
                return super.readType(source);
            }
        };

        mappingConverter.setTypeMapper(typeMapper);//去掉默认mapper添加的_class
        mappingConverter.setCustomConversions(customConversions());//添加自定义的转换器


        return mappingConverter;
    }

    private MongoClient mongo;



    @Bean
    public SaveMongoEventListener mongoEventListener() {
        return new SaveMongoEventListener();
    }

}


