package com.common.mongo;

import com.common.annotation.QueryField;
import com.common.exception.ApplicationException;
import com.common.util.AbstractBaseEntity;
import com.common.util.Money;
import com.common.util.PropertyUtil;
import com.common.util.StringUtils;
import com.common.util.model.OrderTypeEnum;
import com.common.util.model.YesOrNoEnum;
import com.mongodb.client.result.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Administrator on 2017/7/10.
 */
public abstract class AbstractMongoService<T extends AbstractBaseEntity> implements MongoService<T> {
    private static Logger logger = LoggerFactory.getLogger(AbstractMongoService.class);

    @Resource(name = "primary")
    protected MongoTemplate primaryTemplate;

    @Resource(name = "secondary")
    protected MongoTemplate secondaryTemplate;


    protected abstract Class getEntityClass();

    public AbstractMongoService() {
        buildPropertyDescriptor();
    }

    public void save(T entity) {
        if (entity == null) {
            throw new ApplicationException("保存失败，对象未实例化");
        }
        if (entity.getId() != null) {
            up(entity);
            return;
        }
        insert(entity);
    }

    public void insert(T entity) {
        if (entity == null) {
            throw new ApplicationException("保存失败，对象未实例化");
        }
        Date createTime = new Date();
        if (entity.getCreateTime() == null) {
            entity.setCreateTime(createTime);
        }
        if (entity.getUpdateTime() == null) {
            entity.setUpdateTime(createTime);
        }
        entity.setDelStatus(YesOrNoEnum.NO.getValue());
        primaryTemplate.insert(entity);
    }


    public void up(T entity) {
        if (entity == null || entity.getId() == null) {
            throw new ApplicationException("Id不能为空");
        }
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(entity.getId());
        query.addCriteria(criteria);
        entity.setUpdateTime(new Date());
        Update update = addUpdate(entity);
        UpdateResult upsert = primaryTemplate.upsert(query, update, entity.getClass());
        if (upsert.getModifiedCount() == 1 && upsert.isModifiedCountAvailable()) {
            return;
        }
        throw new ApplicationException("mongodb updata error");
    }


    public T findById(String id) {
        return findById(id, false);
    }

    public T findById(String id, boolean trans) {

        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(id);
        query.addCriteria(criteria);
        query.addCriteria(Criteria.where("delStatus").is(YesOrNoEnum.NO.getValue()));
        List<T> ts = null;
        MongoTemplate template = null;
        if (trans) {
            template = primaryTemplate;
        } else {
            template = secondaryTemplate;
        }
        ts = template.find(query, getEntityClass());
        if (ts != null && ts.size() == 1) {
            return ts.get(0);
        }
        throw new ApplicationException("no data to find");
    }

    public void delById(String id) {
        if (id == null) {
            throw new ApplicationException("删除数据出错,id不能为空");
        }
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(id);
        query.addCriteria(criteria);
        primaryTemplate.remove(query, getEntityClass());
    }

    public List<T> query(T entity) {
        return query(entity, false);
    }

    public List<T> query(T entity, boolean trans) {
        if (entity == null) {
            return secondaryTemplate.findAll(getEntityClass());
        }
        entity.setDelStatus(YesOrNoEnum.NO.getValue());
        Query query = buildCondition(entity);
        MongoTemplate template = null;
        if (trans) {
            template = primaryTemplate;
        } else {
            template = secondaryTemplate;
        }
        return template.find(query, getEntityClass());
    }

    public Long queryCount(T entity) {
        return queryCount(entity, false);
    }

    public Long queryCount(T entity, boolean trans) {
        entity.setDelStatus(YesOrNoEnum.NO.getValue());
        Query query = buildCondition(entity);
        MongoTemplate template = null;
        if (trans) {
            template = primaryTemplate;
        } else {
            template = secondaryTemplate;
        }
        return template.count(query, entity.getClass());
    }

    public Page<T> queryByPage(T entity, Pageable pageable) {
        return queryByPage(entity, pageable, false);
    }

    public Page<T> queryByPage(T entity, Pageable pageable, boolean trans) {
        entity.setDelStatus(YesOrNoEnum.NO.getValue());
        Long count = queryCount(entity,trans);
        Query query = buildCondition(entity, pageable);
        MongoTemplate template = null;
        if (trans) {
            template = primaryTemplate;
        } else {
            template = secondaryTemplate;
        }
        List<T> list = template.find(query, getEntityClass());
        Page<T> pagelist = new PageImpl<T>(list, pageable, count);
        return pagelist;
    }

    public Page<T> queryByPage(Query query, Pageable pageable) {
        return queryByPage(query, pageable, false);
    }

    public Page<T> queryByPage(Query query, Pageable pageable, boolean trans) {
        if (pageable.getSort() == null) {
            return queryByPage(query, pageable, "createTime", OrderTypeEnum.DESC, trans);
        }
        MongoTemplate template = null;
        if (trans) {
            template = primaryTemplate;
        } else {
            template = secondaryTemplate;
        }
        long count = template.count(query, getEntityClass());
        List<T> list = template.find(query, getEntityClass());
        Page<T> pagelist = new PageImpl<T>(list, pageable, count);
        return pagelist;
    }

    public Page<T> queryByPage(Query query, Pageable pageable, String orderColum, OrderTypeEnum orderType) {
        return queryByPage(query, pageable, orderColum, orderType, false);
    }

    public Page<T> queryByPage(Query query, Pageable pageable, String orderColum, OrderTypeEnum orderType, boolean trans) {
        Sort.Direction sortType = null;
        if (OrderTypeEnum.ASC == orderType) {
            sortType = Sort.Direction.ASC;
        } else {
            sortType = Sort.Direction.DESC;
        }
        if (StringUtils.isBlank(orderColum)) {
            orderColum = "createTime";
        }
        MongoTemplate template = null;
        if (trans) {
            template = primaryTemplate;
        } else {
            template = secondaryTemplate;
        }
        long count = template.count(query, getEntityClass());
        if (pageable.getSort() == null) {
            Sort sort = new Sort(sortType, orderColum);
            PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
            query.with(pageRequest);
        }
        List<T> list = template.find(query, getEntityClass());

        Page<T> pagelist = new PageImpl<T>(list, pageable, count);
        return pagelist;
    }

    public T findByOne(T entity) {
        return findByOne(entity, false);
    }


    public T findByOne(T entity, boolean trans) {
        entity.setDelStatus(YesOrNoEnum.NO.getValue());
        Query query = buildCondition(entity);
        MongoTemplate template = null;
        if (trans) {
            template = primaryTemplate;
        } else {
            template = secondaryTemplate;
        }
        return template.findOne(query, (Class<T>) getEntityClass());
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
        return buildCondition(entity, null);
    }

    protected Query buildCondition(T entity, Pageable pageable) {
        Query query = new Query();
        Map<String, QueryItem> queryItemMap = new HashMap<>();
        for (Field field : beanPropertyes.values()) {
            Object property = null;
            String descriptorName = field.getName();
            if ((!descriptorName.equals("class")) && (!descriptorName.equals("orderColumn")) && (!descriptorName.equals("orderType"))) {
                try {
                    property = PropertyUtil.getProperty(entity, descriptorName);
                    if (property instanceof Money) {
                        property = ((Money) property).getCent();
                    }
                } catch (Exception e) {
                    logger.error("buildCondition error", e);
                }
                if (property != null) {
                    QueryField annotation = field.getAnnotation(QueryField.class);
                    if (annotation != null) {
                        QueryType type = annotation.type();
                        String typeName = annotation.name();
                        String propertyName;
                        if (StringUtils.isNotBlank(typeName)) {
                            propertyName = annotation.name();
                        } else {
                            propertyName = descriptorName;
                        }
                        QueryItem queryItem = queryItemMap.get(propertyName);
                        if (queryItem == null) {
                            queryItem = new QueryItem(propertyName, property, type);
                        } else {
                            queryItem.addCondition(type, property);
                        }
                        queryItemMap.put(propertyName, queryItem);
                    } else {
                        QueryItem queryItem = queryItemMap.get(descriptorName);
                        if (queryItem == null) {
                            queryItem = new QueryItem(descriptorName, property);
                        } else {
                            throw new ApplicationException("property duplication");
                        }
                        queryItemMap.put(descriptorName, queryItem);
                    }
                }
            }
        }

        Set<String> properties = queryItemMap.keySet();
        for (String pro : properties) {
            QueryItem queryItem = queryItemMap.get(pro);
            Criteria criteria = Criteria.where(queryItem.getName());
            Set<QueryType> queryTypes = queryItem.getConditions().keySet();
            for (QueryType type : queryTypes) {
                switch (type) {
                    case EQ:
                        criteria.is(queryItem.getConditions().get(type));
                        break;
                    case LT:
                        criteria.lt(queryItem.getConditions().get(type));
                        break;
                    case LTE:
                        criteria.lte(queryItem.getConditions().get(type));
                        break;
                    case GT:
                        criteria.gt(queryItem.getConditions().get(type));
                        break;
                    case GTE:
                        criteria.gte(queryItem.getConditions().get(type));
                        break;
                    case NE:
                        criteria.ne(queryItem.getConditions().get(type));
                        break;
                    case LIKE:
                        criteria.regex((String) queryItem.getConditions().get(type));
                        break;
                    case IN:
                        Object[] values = null;
                        Object o = queryItem.getConditions().get(type);
                        if (o instanceof ArrayList) {
                            values = ((ArrayList) o).toArray();
                        }
                        if (o.getClass().isArray()) {
                            values = (Object[]) o;
                        }
                        if (o instanceof Map) {
                            values = ((Map) o).entrySet().toArray();
                        }
                        criteria = criteria.in(values);
                        break;
                }
            }
            query.addCriteria(criteria);
        }
        if (pageable != null) {
            if (pageable instanceof PageRequest) {
                Sort orders = buildSort(entity);
                PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), orders);
                query.with(pageRequest);
                query.limit(pageable.getPageSize());
            }
        } else {
            query.with(buildSort(entity));
            query.limit(50);
        }
        return query;
    }

    private Sort buildSort(T entity) {
        Sort orders = null;
        if (StringUtils.isBlank(entity.getOrderColumn())) {
            entity.setOrderColumn("createTime");
        }
        if (entity.getOrderType() == null) {
            entity.setOrderType(OrderTypeEnum.ASC);
        }
        if (entity.getOrderType().getValue() == 1) {
            orders = new Sort(Sort.Direction.ASC, entity.getOrderColumn());
        }
        if (entity.getOrderType().getValue() == 2) {
            orders = new Sort(Sort.Direction.DESC, entity.getOrderColumn());
        }
        return orders;
    }

    protected Update addUpdate(T entity) {
        Update update = new Update();
        for (Field descriptor : beanPropertyes.values()) {
            Transient annotation = descriptor.getAnnotation(Transient.class);
            if (annotation != null) {
                continue;
            }
            String descriptorName = descriptor.getName();
            if ((!descriptorName.equals("class")) && (!descriptorName.equals("id"))) {
                Object property = null;
                try {
                    property = PropertyUtil.getProperty(entity, descriptorName);
                    if (property instanceof Money) {
                        property = ((Money) property).getCent();
                    }
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
}

class QueryItem {
    private String name;

    private Map<QueryType, Object> conditions = new HashMap<>();

    public QueryItem(String name, Object value) {
        this.name = name;
        conditions.put(QueryType.EQ, value);
    }

    public QueryItem(String name, Object value, QueryType queryType) {
        this.name = name;
        conditions.put(queryType, value);
    }

    public void addCondition(QueryType type, Object value) {
        conditions.put(type, value);
    }

    public QueryItem() {
    }

    public Map<QueryType, Object> getConditions() {
        return conditions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}