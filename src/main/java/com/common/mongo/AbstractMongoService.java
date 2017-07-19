package com.common.mongo;

import com.common.annotation.QueryField;
import com.common.exception.ApplicationException;
import com.common.exception.BizException;
import com.common.util.AbstractBaseEntity;
import com.common.util.PropertyUtil;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Administrator on 2017/7/10.
 */
public abstract class AbstractMongoService<T extends AbstractBaseEntity> implements MongoService<T> {
    private static Logger logger = Logger.getLogger(AbstractMongoService.class);

    private MongoTemplate template;

    protected abstract Class<T> getEntityClass();

    public AbstractMongoService() {
        buildPropertyDescriptor();
    }

    public Long save(T entity) {
        if (entity == null) {
            throw new ApplicationException("保存失败，对象未实例化");
        }
        if (entity.getId() != null) {
            up(entity);
            return entity.getId();
        }
        Date createTime = new Date();
        if (entity.getCreateTime() == null) {
            entity.setCreateTime(createTime);
        }
        if (entity.getUpdateTime() == null) {
            entity.setUpdateTime(createTime);
        }
        entity.setDelStatus(YesOrNoEnum.NO.getValue());
        template.insert(entity);
        return entity.getId();
    }


    private void up(T entity) {
        if (entity == null || entity.getId() == null) {
            throw new ApplicationException("Id不能为空");
        }
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(entity.getId());
        query.addCriteria(criteria);
        entity.setUpdateTime(new Date());
        Update update = addUpdate(entity);
        template.findAndModify(query, update, entity.getClass());
    }


    public T findById(Long id) {
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(id);
        query.addCriteria(criteria);
        return template.findById(id, getEntityClass());
    }

    public void delById(Long id) {
        if (id == null) {
            throw new ApplicationException("删除数据出错,id不能为空");
        }
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(id);
        query.addCriteria(criteria);
        Constructor<?>[] constructors = getEntityClass().getConstructors();
        T entity = null;
        try {
            entity = (T) constructors[0].newInstance();
        } catch (Exception e) {
            throw new ApplicationException("删除mongodb数据实败", e);
        }

        entity.setDelStatus(YesOrNoEnum.YES.getValue());
        Update update = addUpdate(entity);
        template.findAndModify(query, update, getEntityClass());
    }

    public List<T> query(T entity) {
        if (entity == null) {
            return template.findAll(getEntityClass());
        }
        entity.setDelStatus(YesOrNoEnum.NO.getValue());
        Query query = buildCondition(entity);
        return template.find(query, getEntityClass());
    }

    public Long queryCount(T entity) {
        Query query = buildCondition(entity);
        entity.setDelStatus(YesOrNoEnum.NO.getValue());
        return template.count(query, entity.getClass());
    }

    public Page<T> queryByPage(T entity, Pageable pageable) {
        entity.setDelStatus(YesOrNoEnum.YES.getValue());
        Query query = buildCondition(entity);
        Long count = template.count(query, entity.getClass());
        List<T> list = template.find(query, getEntityClass());
        Page<T> pagelist = new PageImpl<T>(list, pageable, count);
        return pagelist;
    }

    public T findByOne(T entity) {
        Query query = buildCondition(entity);
        entity.setDelStatus(YesOrNoEnum.NO.getValue());
        return template.findOne(query, getEntityClass());
    }

    private Map<String, Field> beanPropertyes = new HashMap<>();

    private void buildPropertyDescriptor() {
        List<Class> types = new ArrayList<>();
        Class currentClass = getEntityClass();
        while (currentClass != AbstractBaseEntity.class) {
            types.add(currentClass);
            currentClass = currentClass.getSuperclass();
        }
        types.add(AbstractBaseEntity.class);
        for (Class classz : types) {
            Field[] propertyDescriptors = classz.getDeclaredFields();
            for (Field descriptor : propertyDescriptors) {
                beanPropertyes.put(descriptor.getName(), descriptor);
            }
        }
        beanPropertyes.remove("serialVersionUID");
        beanPropertyes.remove("uuid");
    }

    protected Query buildCondition(T entity) {
        Query query = new Query();
        for (Field field : beanPropertyes.values()) {
            Object property = null;
            String descriptorName = field.getName();
            if ((!descriptorName.equals("class")) && (!descriptorName.equals("orderColumn")) && (!descriptorName.equals("orderTpe"))) {
                try {
                    property = PropertyUtil.getProperty(entity, descriptorName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (property != null) {
                    Criteria criteria = new Criteria();
                    if (field != null) {
                        QueryField annotation = field.getAnnotation(QueryField.class);
                        if (annotation != null) {
                            QueryType type = annotation.type();
                            String typeName = annotation.name();
                            String propertyName;
                            if (type != null) {
                                if (StringUtils.isNotBlank(typeName)) {
                                    propertyName = annotation.name();
                                } else {
                                    propertyName = descriptorName;
                                }
                                switch (type) {
                                    case EQ:
                                        criteria = Criteria.where(propertyName).is(property);
                                        break;
                                    case LT:
                                        criteria = Criteria.where(propertyName).lt(property);
                                        break;
                                    case LTE:
                                        criteria = Criteria.where(propertyName).lte(property);
                                        break;
                                    case GT:
                                        criteria = Criteria.where(propertyName).gt(property);
                                        break;
                                    case GTE:
                                        criteria = Criteria.where(propertyName).gte(property);
                                        break;
                                    case NE:
                                        criteria = Criteria.where(propertyName).ne(property);
                                        break;
                                    case LIKE:
                                        criteria = Criteria.where(propertyName).regex(property.toString());
                                        break;
                                }
                            } else {
                                criteria = Criteria.where(descriptorName).is(property);
                            }
                        }
                    } else {
                        criteria = Criteria.where(descriptorName).is(property);
                    }
                    query.addCriteria(criteria);
                }
            }
        }
        if (StringUtils.isNotBlank(entity.getOrderColumn())) {
            if (entity.getOrderTpe() == null) {
                query.with(new Sort(Sort.Direction.ASC, entity.getOrderColumn()));
            } else {
                if (entity.getOrderTpe() == 1) {
                    query.with(new Sort(Sort.Direction.ASC, entity.getOrderColumn()));
                }
                if (entity.getOrderTpe() == 2) {
                    query.with(new Sort(Sort.Direction.DESC, entity.getOrderColumn()));
                }
            }
        }
        return query;
    }

    private Update addUpdate(T entity) {
        Update update = new Update();
        for (Field descriptor : beanPropertyes.values()) {
            String descriptorName = descriptor.getName();
            if ((!descriptorName.equals("class")) && (!descriptorName.equals("id"))) {
                Object property = null;
                try {
                    property = PropertyUtil.getProperty(entity, descriptorName);
                } catch (Exception e) {
                    throw new ApplicationException("更新或添加mongodb失败", e);
                }
                if (property != null) {
                    update.set(descriptorName, property);
                }
            }
        }
        return update;
    }

    public void setTemplate(MongoTemplate template) {
        this.template = template;
    }
}
