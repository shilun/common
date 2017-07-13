package com.common.mongo;

import com.common.annotation.QueryField;
import com.common.exception.ApplicationException;
import com.common.exception.BizException;
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
import org.springframework.data.mongodb.core.ScriptOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.script.NamedMongoScript;


import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/10.
 */
public class AbstractMongoService<T extends AbstractBaseEntity> implements Serializable {


    private MongoTemplate template;

    private static Logger logger = Logger.getLogger(AbstractMongoService.class);

    private static String userName;
    private static String password;
    private static String dbName = "estate";
    private static String url = "192.168.89.47";
    private static Integer port = 27017;


    public void add(T entity) {
        MongoTemplate mongoTemplate=setTemplate(template);
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "createTime"));
        Long id=1l;
        List<T> ts = mongoTemplate.find(query, (Class<T>) entity.getClass());
        if (!ts.isEmpty()){
            id=ts.get(0).getId();
            id++;
        }
        entity.setId(id);
        Date createTime = new Date();
        if (entity.getCreateTime() == null) {
            entity.setCreateTime(createTime);
        }
        if (entity.getUpdateTime() == null) {
            entity.setUpdateTime(createTime);
        }
        entity.setDelStatus(YesOrNoEnum.NO.getValue());
        mongoTemplate.insert(entity);
    }


    public void up(T entity) {
        if(entity.getId()==null){
            throw new BizException("Id.null","Id不能为空");
        }
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(entity.getId());
        query.addCriteria(criteria);
        entity.setUpdateTime(new Date());
        Update update = addUpdate(entity);
        setTemplate(template).findAndModify(query, update, entity.getClass());
    }

    public void save(T entity) {

    }
    public T findById(Long id,T entity) {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(id);
        query.addCriteria(criteria);
        return setTemplate(template).findById(id, (Class<T>) entity.getClass());
    }

    public void delById(T entity) {
        if(entity.getId()==null){
            throw new BizException("Id.null","Id不能为空");
        }
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(entity.getId());
        query.addCriteria(criteria);
        entity.setDelStatus(YesOrNoEnum.YES.getValue());
        Update update = addUpdate(entity);
        setTemplate(template).findAndModify(query, update, entity.getClass());
    }

    public List<T> query(T entity) {
        if (entity == null) {
            return setTemplate(template).findAll((Class<T>) entity.getClass());
        }
        entity.setDelStatus(YesOrNoEnum.NO.getValue());
        Query query = addCondition(entity);
        return setTemplate(template).find(query, (Class<T>) entity.getClass());
    }

    public Long queryCount(T entity) {
        Query query = addCondition(entity);
        entity.setDelStatus(YesOrNoEnum.NO.getValue());
        return setTemplate(template).count(query, entity.getClass());
    }

    public Page<T> queryByPage(T entity, Pageable pageable) {
        entity.setDelStatus(YesOrNoEnum.YES.getValue());
        Query query = addCondition(entity);
        Long count = setTemplate(template).count(query, entity.getClass());
        List<T> list = setTemplate(template).find(query, (Class<T>) entity.getClass());
        Page<T> pagelist = new PageImpl<T>(list, pageable, count);
        return pagelist;
    }

    public T findByOne(T entity) {
        Query query = addCondition(entity);
        entity.setDelStatus(YesOrNoEnum.NO.getValue());
        return setTemplate(template).findOne(query, (Class<T>) entity.getClass());
    }

    public MongoTemplate setTemplate(MongoTemplate template) {
        Mongo mongo = new Mongo(url, port);
        return new MongoTemplate(mongo, dbName);
    }

    Query addCondition(T entity) {
        Query query = new Query();
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(entity.getClass());
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            Object property = null;
            String descriptorName = descriptor.getName();
            if((!descriptorName.equals("class"))&&(!descriptorName.equals("orderColumn"))&&(!descriptorName.equals("orderTpe"))){
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
                    Field field= null;
                    try {
                        field = entity.getClass().getDeclaredField(descriptorName);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    Criteria criteria = new Criteria();
                    if(field!=null){
                        QueryField annotation = field.getAnnotation(QueryField.class);
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
                    }else{
                        criteria = Criteria.where(descriptorName).is(property);
                        query.addCriteria(criteria);
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(entity.getOrderColumn())) {
            if(entity.getOrderTpe()==null){
                query.with(new Sort(Sort.Direction.ASC, entity.getOrderColumn()));
            }else{
                if(entity.getOrderTpe()==1){
                    query.with(new Sort(Sort.Direction.ASC, entity.getOrderColumn()));
                }
                if(entity.getOrderTpe()==2){
                    query.with(new Sort(Sort.Direction.DESC, entity.getOrderColumn()));
                }
            }
        }
        return query;
    }

    Update addUpdate(T entity) {
        Update update = new Update();
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(entity.getClass());
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String descriptorName = descriptor.getName();
            if((!descriptorName.equals("class"))&&(!descriptorName.equals("id"))){
                Object property = null;
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
                    update.set(descriptorName,property);
                }
            }
        }
        return update;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        AbstractMongoService.userName = userName;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        AbstractMongoService.password = password;
    }

    public static String getDbName() {
        return dbName;
    }

    public static void setDbName(String dbName) {
        AbstractMongoService.dbName = dbName;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        AbstractMongoService.url = url;
    }

    public static Integer getPort() {
        return port;
    }

    public static void setPort(Integer port) {
        AbstractMongoService.port = port;
    }
}
