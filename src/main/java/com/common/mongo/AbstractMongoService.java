package com.common.mongo;

import com.common.annotation.QueryField;
import com.common.exception.ApplicationException;
import com.common.util.*;
import com.common.util.model.OrderTypeEnum;
import com.common.util.model.YesOrNoEnum;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.annotation.Annotation;
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

    @Resource
    private MongoMappingContext mongoMappingContext;

    protected abstract Class getEntityClass();

    public AbstractMongoService() {

    }

    @PostConstruct
    public void init() {
        if (getEntityClass() == null) {
            throw new ApplicationException(getEntityClass().getSimpleName() + "服务获取entityClassError:null");
        }
        buildPropertyDescriptor();
        try {
            buildIndex(primaryTemplate.getCollectionName(getEntityClass()));
        } catch (Exception e) {
            logger.error(getEntityClass().getSimpleName() + " 构建mongodb索引出错，请检查索引配置", e);
        }
    }
    /**
     * 创建 索引
     *
     * @param collectionName
     */
    protected void buildIndex(String collectionName) {
        Class entityClass = getEntityClass();
        Annotation[] type = entityClass.getAnnotationsByType(CompoundIndexes.class);
        if (type != null && type.length == 1) {
            CompoundIndexes index = (CompoundIndexes) type[0];
            Map<String, CompoundIndex> indexMap = new HashMap<>();

            for (CompoundIndex item : index.value()) {
                indexMap.put(item.name(), item);
            }
            boolean b = primaryTemplate.collectionExists(collectionName);
            if (b == false) {
                primaryTemplate.createCollection(collectionName);
            }
            List<IndexInfo> indexInfos = primaryTemplate.indexOps(collectionName).getIndexInfo();

            for (String content : indexMap.keySet()) {
                boolean exist = false;
                for (IndexInfo info : indexInfos) {
                    if (content.equals(info.getName())) {
                        exist = true;
                    }
                }
                if (!exist) {
                    CompoundIndex compoundIndex = indexMap.get(content);
                    Document document = new Document();
                    document.put("name", compoundIndex.name());
                    document.put("unique", compoundIndex.unique());
                    primaryTemplate.indexOps(collectionName).ensureIndex(new IndexDefinition() {
                        @Override
                        public Document getIndexKeys() {
                            return Document.parse(compoundIndex.def());
                        }

                        @Override
                        public Document getIndexOptions() {
                            return document;
                        }
                    });
                }
            }
        } else {
            boolean b = primaryTemplate.collectionExists(collectionName);
            if (b == false) {
                primaryTemplate.createCollection(collectionName);
            }
        }
    }


    @Override
    public Criteria buildCondition(String property, QueryType type, Object value) {
        return type.build(property, value);
    }

    @Override
    public List<T> queryAll() {
        return queryAll(false);
    }

    @Override
    public List<T> queryAll(boolean trans) {
        MongoTemplate template = null;
        if (trans) {
            template = primaryTemplate;
        } else {
            template = secondaryTemplate;
        }
        return template.findAll(getEntityClass());
    }

    @Override
    public List<T> query(Query query) {
        return query(query, false);
    }

    @Override
    public List<T> query(Query query, boolean trans) {
        MongoTemplate template = null;
        if (trans) {
            template = primaryTemplate;
        } else {
            template = secondaryTemplate;
        }
        return template.find(query, getEntityClass());
    }

    @Override
    public void inc(String id, String property, IncType type) {
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(id);
        query.addCriteria(criteria);
        Update update = new Update();
        int size = 0;
        if (IncType.sub == type) {
            size = -1;
        } else {
            size = 1;
        }
        update.inc(property, size);
        UpdateResult updateResult = primaryTemplate.updateFirst(query, update, getEntityClass());
        if(updateResult.getModifiedCount()==1){
            return;
        }
        throw new ApplicationException("mongodb updata error");
    }

    @Override
    public void inc(String id, String property, Integer size) {
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(id);
        query.addCriteria(criteria);
        Update update = new Update();
        update.inc(property, size);
        UpdateResult updateResult = primaryTemplate.updateFirst(query, update, getEntityClass());
        if(updateResult.getModifiedCount()==1){
            return;
        }
        throw new ApplicationException("mongodb updata error");
    }

    public boolean exist(T entity){
        entity.setDelStatus(YesOrNoEnum.NO);
        Query query = buildCondition(entity, PageRequest.of(0,1));
        return secondaryTemplate.exists(query,getEntityClass());
    }

    public void save(T entity) {
        if (entity == null) {
            throw new ApplicationException("保存失败，对象未实例化");
        }
        if (StringUtils.isBlank(entity.getId())) {
            entity.setId(null);
        }
        if (StringUtils.isNotBlank(entity.getId())) {
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
        if (StringUtils.isNotBlank(entity.getId())) {
            StringUtils.checkId(entity.getId());
        }
        entity.setDelStatus(YesOrNoEnum.NO);
        primaryTemplate.insert(entity);
    }


    public void up(T entity) {
        if (entity == null || entity.getId() == null) {
            throw new ApplicationException("Id不能为空");
        }
        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(entity.getId());
        query.addCriteria(criteria);
        entity.setUpdateTime(new Date());
        Update update = addUpdate(entity);
        UpdateResult result = primaryTemplate.updateFirst(query, update, entity.getClass());
        if (result.getMatchedCount() == 1) {
            return;
        }
        throw new ApplicationException("mongodb updata error");
    }

    /**
     * 查询第一条记录
     * @param entity
     * @param trans
     * @return
     */
    public T queryFirst(T entity,boolean trans){
        entity.setDelStatus(YesOrNoEnum.NO);
        Query query = buildCondition(entity, PageRequest.of(0,1));
        MongoTemplate template = null;
        if (trans) {
            template = primaryTemplate;
        } else {
            template = secondaryTemplate;
        }
        List<T> list = template.find(query, getEntityClass());
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }

    public T findById(String id) {
        return findById(id, false);
    }

    public T findById(String id, boolean trans) {

        Query query = new Query();
        Criteria criteria = Criteria.where("id").is(id);
        query.addCriteria(criteria);
        query.addCriteria(Criteria.where("delStatus").is(YesOrNoEnum.NO));
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
        Criteria criteria = Criteria.where("id").is(id);
        query.addCriteria(criteria);
        DeleteResult remove = primaryTemplate.remove(query, getEntityClass());
        if(remove.getDeletedCount()==1){
            return;
        }
        throw new ApplicationException("no data to find");
    }

    public List<T> query(T entity) {
        return query(entity, false);
    }

    public List<T> query(T entity, boolean trans) {
        if (entity == null) {
            return secondaryTemplate.findAll(getEntityClass());
        }
        entity.setDelStatus(YesOrNoEnum.NO);
        Query query = buildCondition(entity);
        MongoTemplate template = null;
        if (trans) {
            template = primaryTemplate;
        } else {
            template = secondaryTemplate;
        }
        return template.find(query, getEntityClass());
    }

    @Override
    public Long queryCount(Query query) {
        return secondaryTemplate.count(query, getEntityClass());
    }

    public Long queryCount(T entity) {
        return queryCount(entity, false);
    }

    public Long queryCount(T entity, boolean trans) {
        entity.setDelStatus(YesOrNoEnum.NO);
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
        entity.setDelStatus(YesOrNoEnum.NO);
        Long count = queryCount(entity, trans);
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
        if (pageable.getSort() == Sort.unsorted()) {
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
        if (pageable.getSort() == Sort.unsorted()) {
            Sort sort = Sort.by(sortType, orderColum);
            PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
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
        entity.setDelStatus(YesOrNoEnum.NO);
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

    protected void buildPropertyDescriptor() {

        List<Class> types = new ArrayList<>();
        Class currentClass = getEntityClass();
        if (currentClass == null) {
            logger.error(this.getClass().getName() + ".getEntityClass.null");
        }
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

    public Query buildCondition(T entity) {
        return buildCondition(entity, null);
    }

    public Query buildCondition(T entity, Pageable pageable) {
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
                PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), orders);
                query.with(pageRequest);
                query.limit(pageable.getPageSize());
            }
        } else {
            query.with(buildSort(entity));
            query.limit(50);
        }
        return query;
    }

    protected Sort buildSort(T entity) {
        Sort orders = null;
        if (StringUtils.isBlank(entity.getOrderColumn())) {
            entity.setOrderColumn("createTime");
        }
        if (entity.getOrderType() == null) {
            entity.setOrderType(OrderTypeEnum.DESC);
        }
        if (entity.getOrderType() == OrderTypeEnum.ASC) {
            orders = Sort.by(Sort.Direction.ASC, entity.getOrderColumn());
        }
        if (entity.getOrderType() == OrderTypeEnum.DESC) {
            orders = Sort.by(Sort.Direction.DESC, entity.getOrderColumn());
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

    /**
     * 对象转换
     *
     * @param typeClass 目标类型
     * @param source    源类型
     * @param <T>
     * @return
     */
    protected <T> T clone(Class<T> typeClass, Object source) {
        return BeanCoper.copyProperties(typeClass, source);
    }

    /**
     * list数据转换
     *
     * @param typeClass  目标类型
     * @param sourcepage 源类型
     * @param <T>
     * @return
     */
    protected <T> List<T> clone(Class<T> typeClass, List<?> sourcepage) {
        return BeanCoper.copyList(typeClass, sourcepage);
    }

    /**
     * 分页数据转换
     *
     * @param typeClass  目标类型
     * @param sourcepage 源类型
     * @param <T>
     * @return
     */
    protected <T> Page<T> clone(Class<T> typeClass, Page<?> sourcepage) {
        return BeanCoper.copyPage(typeClass, sourcepage);
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
