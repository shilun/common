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

import com.common.mongo.SaveMongoEventListener;
import com.common.util.StringUtils;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Mongo.
 */
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongodbUrl;

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }

    @Bean("primary")
    public MongoTemplate mongoPrimaryTemplate(MongoDatabaseFactory databaseFactory, MappingMongoConverter converter) throws Exception {
        if (StringUtils.isBlank(mongodbUrl)) {
            throw new Exception("mongodb load error url" + mongodbUrl);
        }
        MongoTemplate mongoTemplate = new MongoTemplate(databaseFactory, converter);
        mongoTemplate.setReadPreference(ReadPreference.primary());
        mongoTemplate.setWriteConcern(WriteConcern.MAJORITY);
        return mongoTemplate;
    }

    @Bean("secondary")
    public MongoTemplate mongoSecondaryTemplate(MongoDatabaseFactory databaseFactory, MappingMongoConverter converter) throws Exception {
        if (StringUtils.isBlank(mongodbUrl)) {
            throw new Exception("mongodb load error url" + mongodbUrl);
        }
        MongoTemplate mongoTemplate = new MongoTemplate(databaseFactory, converter);
        mongoTemplate.setReadPreference(ReadPreference.secondary());
        mongoTemplate.setWriteConcern(WriteConcern.MAJORITY);
        return mongoTemplate;
    }

    @Bean
    public SaveMongoEventListener mongoEventListener() {
        return new SaveMongoEventListener();
    }


    @Override
    protected String getDatabaseName() {
        return mongoClient().listDatabaseNames().first();
    }

    public @Bean
    MongoClient mongoClient() {
        return MongoClients.create(mongodbUrl);
    }
}


