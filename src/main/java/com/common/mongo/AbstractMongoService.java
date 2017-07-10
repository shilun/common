package com.common.mongo;

import com.common.annotation.QueryField;
import com.common.util.AbstractBaseEntity;
import com.common.util.PropertyUtil;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;
import com.mongodb.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;


import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/10.
 */
public abstract class AbstractMongoService<T extends AbstractBaseEntity> implements Serializable {

    private MongoOperations operations;

    private MongoTemplate template;

    private static Logger logger = Logger.getLogger(AbstractMongoService.class);
    private static MongoClient client = null;
    private List<ServerAddress> addressList = null;
    private boolean isSave = true;
    private int connectionPerHost = 50;
    private int connectionTimeout = 10000;
    private static String userName;
    private static String password;
    private static String dbName;
    public void initMongo() {
        try {
            MongoClientOptions.Builder e = new MongoClientOptions.Builder();
            e.connectionsPerHost(50);
            e.maxWaitTime(120000);
            e.connectTimeout(this.connectionTimeout);
            e.threadsAllowedToBlockForConnectionMultiplier(50);
            MongoClientOptions options = e.build();
            ArrayList credentialsList = new ArrayList();
            if (!StringUtils.isBlank(userName) && !StringUtils.isBlank(password)) {
                MongoCredential mc = MongoCredential.createCredential(userName, dbName, password.toCharArray());
                credentialsList.add(mc);
                client = new MongoClient(this.addressList, credentialsList, options);
            } else {
                client = new MongoClient(this.addressList, options);
            }
        } catch (Exception var5) {
            logger.error("mongodb 连接出错", var5);
        }

    }
    public static DB getTheDB() {
        return client.getDB(dbName);
    }

    public String add(T entity) {
        initMongo();
        DB db=getTheDB();
        operations=new MongoTemplate(new Mongo(),dbName);
        Object getNextSequence = operations.scriptOps().call("getNextSequence", entity.getClass().getSimpleName());
        entity.setId((Long) getNextSequence);
        operations.insert(entity);
        return "";
    }

    ;

    public void up(T entity) {

    }

    public void save(T entity) {
    }

    public T findById(Long entity) {
        return null;
    }

    public void delById(Long entity) {
    }

    public List<T> query(T entity) {
        if(entity==null){
            return  operations.findAll((Class<T>)entity.getClass());
        }
        Query query = addCondition(entity);
        entity.setDelStatus(YesOrNoEnum.NO.getValue());
        return operations.find(query,(Class<T>)entity.getClass());
    }

    public int queryCount(T entity) {
        return 0;
    }

    public Page<T> queryByPage(T entity, Pageable pageable) {
        Query query = addCondition(entity);
        Long count = operations.count(query, entity.getClass());
        List<T> list = operations.find(query, (Class<T>) entity.getClass());
        Page<T> pagelist = new PageImpl<T>(list, pageable, count);
        return pagelist;
    }

    public T findByOne(T entity) {
        return null;
    }

    public void setTemplate(MongoTemplate template) {
        this.template = template;
    }

    Query addCondition(T entity) {
        Query query = new Query();
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(entity.getClass());
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            Object property = null;
            String descriptorName = descriptor.getName();
            try {
                property = PropertyUtil.getProperty(entity, descriptorName);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if (property != null) {
                Field field = FieldUtils.getField(entity.getClass(), descriptorName);
                QueryField annotation = field.getAnnotation(QueryField.class);
                Criteria criteria = new Criteria();
                if (annotation != null) {
                    QueryType type = annotation.type();
                    String typeName = annotation.name();
                    if (type != null)
                        if (StringUtils.isNotBlank(typeName)) {
                            if (type != null) {
                                if (type == QueryType.EQ) {
                                    criteria = Criteria.where(typeName).is(property);
                                }
                                if (type == QueryType.GT) {
                                    criteria = Criteria.where(typeName).gt(property);
                                }
                                if (type == QueryType.LT) {
                                    criteria = Criteria.where(typeName).lt(property);
                                }
                                if (type == QueryType.GTE) {
                                    criteria = Criteria.where(typeName).gte(property);
                                }
                                if (type == QueryType.LTE) {
                                    criteria = Criteria.where(typeName).lte(property);
                                }
                                if (type == QueryType.LIKE) {
                                    criteria = Criteria.where(typeName).regex(property.toString());
                                }
                                if (type == QueryType.NE) {
                                    criteria = Criteria.where(typeName).ne(property);
                                }
                            } else {
                                criteria = Criteria.where(typeName).is(property);
                            }
                        } else {
                            if (type != null) {
                                if (type == QueryType.EQ) {
                                    criteria = Criteria.where(descriptorName).is(property);
                                }
                                if (type == QueryType.GT) {
                                    criteria = Criteria.where(descriptorName).gt(property);
                                }
                                if (type == QueryType.LT) {
                                    criteria = Criteria.where(descriptorName).lt(property);
                                }
                                if (type == QueryType.GTE) {
                                    criteria = Criteria.where(descriptorName).gte(property);
                                }
                                if (type == QueryType.LTE) {
                                    criteria = Criteria.where(descriptorName).lte(property);
                                }
                                if (type == QueryType.LIKE) {
                                    criteria = Criteria.where(typeName).regex(property.toString());
                                }
                            }
                            if (type == QueryType.NE) {
                                criteria = Criteria.where(descriptorName).ne(property);
                            }
                        }
                    else {
                        criteria = Criteria.where(descriptorName).is(property);
                    }
                }
                query.addCriteria(criteria);
            }

        }
        if (StringUtils.isNotBlank(entity.getOrderColumn())) {
            query.with(new Sort(entity.getOrderTpe(), entity.getOrderColumn()));
        }
        return query;
    }
}
