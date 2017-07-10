package com.common.mongo;

import com.common.util.AbstractBaseEntity;
import com.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/10.
 */
public abstract class AbstractMongoService<T extends AbstractBaseEntity> implements Serializable {

    private MongoOperations operations;

    private MongoTemplate template;

    public String add(T entity) {
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
        //Query.query(new Criteria())
        // operations.find()
        return null;
    }

    public int queryCount(T entity) {
        return 0;
    }

    public Page<T> queryByPage(T entity, Pageable pageable) {
        Query query = new Query();
        query.with(pageable);
        if (StringUtils.isNotBlank(entity.getOrderColumn())) {
            query.with(new Sort(entity.getOrderTpe(), entity.getOrderColumn()));
        }
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
}
