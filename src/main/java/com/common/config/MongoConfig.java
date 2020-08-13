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
import com.mongodb.client.MongoClients;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.util.TypeInformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Mongo.
 */
@Configuration
@Conditional(MongodbCondition.class)
public class MongoConfig {


    @Value("${spring.data.mongodb.uri}")
    private String mongodbUri;

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(){
        return new SimpleMongoClientDatabaseFactory(mongodbUri);
    }

    @Bean("primary")
    public MongoTemplate mongoPrimaryTemplate(MongoDatabaseFactory mongoDatabaseFactory, MappingMongoConverter converter) throws Exception {

        MongoTemplate mongoTemplate = new MongoTemplate(mongoDatabaseFactory, converter);
        mongoTemplate.setReadPreference(ReadPreference.primary());
        mongoTemplate.setWriteConcern(WriteConcern.MAJORITY);
        return mongoTemplate;
    }

    @Bean("secondary")
    public MongoTemplate mongoSecondaryTemplate(MongoDatabaseFactory mongoDatabaseFactory, MappingMongoConverter converter) throws Exception {

        MongoTemplate mongoTemplate = new MongoTemplate(mongoDatabaseFactory, converter);
        mongoTemplate.setReadPreference(ReadPreference.secondary());
        mongoTemplate.setWriteConcern(WriteConcern.MAJORITY);
        return mongoTemplate;
    }
    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter> list = new ArrayList();
        list.add(new MoneyToLongConvert());
        list.add(new LongToMoneyConvert());
        return new MongoCustomConversions(list);
    }
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


}


