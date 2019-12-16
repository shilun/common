package com.common.mongo;

import com.common.util.AbstractBaseEntity;
import com.common.util.model.OrderTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */
public interface MongoService<T extends AbstractBaseEntity> {
    /**
     * 数据
     */
    public enum IncType{
        /**
         * 加
         */
        add,
        /**
         * 减
         */
        sub
    }

    /**
     * 计数
     * @param id 标识
     * @param property 属性
     * @param type 类型
     */
    public void inc(String id,String property,IncType type);

    /**
     * 增加计数
     * @param id
     * @param property
     * @param size
     */
    public void inc(String id, String property, Integer size);
    /***
     * 添加或保存
     * @param entity
     * @return
     */
    public void save(T entity);
    /***
     * 添加
     * @param entity
     * @return
     */
    public void insert(T entity);

    /***
     * 根据id查询
     * @param id
     * @return
     */
    public T findById(String id);
    /***
     * 根据id查询
     * @param id
     * @return
     */
    public T findById(String id,boolean trans);

    /***
     * 根据条件查询单条记录
     * @param entity
     * @return
     */
    public T findByOne(T entity);
    /***
     * 根据条件查询单条记录
     * @param entity
     * @return
     */
    public T findByOne(T entity,boolean trans);

    /**
     * 根据id 删除
     * @param id
     */
    public void delById(String id);

    /**
     * 模糊查询
     * @param entity
     * @return
     */
    public List<T> query(T entity);
    /**
     * 模糊查询
     * @param entity
     * @return
     */
    public List<T> query(T entity,boolean trans);

    /**
     * 查询总条数
     * @param entity
     * @return
     */
    public Long queryCount(T entity);
    /**
     * 查询总条数
     * @param entity
     * @return
     */
    public Long queryCount(T entity,boolean trans);

    /***
     * 分页查询 默认不给排序查询 跟据 createTime 倒序
     * @param entity
     * @param pageable
     * @return
     */
    public Page<T> queryByPage(T entity, Pageable pageable);
    /***
     * 分页查询 默认不给排序查询 跟据 createTime 倒序
     * @param entity
     * @param pageable
     * @return
     */
    public Page<T> queryByPage(T entity, Pageable pageable,boolean trans);


    /**
     * 分页查询 默认不给排序查询 跟据 createTime 倒序
     * @param query
     * @param pageable
     * @return
     */
    public Page<T> queryByPage(Query query,Pageable pageable);
    /**
     * 分页查询 默认不给排序查询 跟据 createTime 倒序
     * @param query
     * @param pageable
     * @return
     */
    public Page<T> queryByPage(Query query,Pageable pageable,boolean trans);

    /**
     * 分页查询 默认不给排序查询 跟据 createTime 倒序
     * @param query
     * @param pageable
     * @param orderColomn
     * @param orderType
     * @return
     */
    public Page<T> queryByPage(Query query, Pageable pageable, String orderColomn, OrderTypeEnum orderType);
    /**
     * 分页查询 默认不给排序查询 跟据 createTime 倒序
     * @param query
     * @param pageable
     * @param orderColomn
     * @param orderType
     * @return
     */
    public Page<T> queryByPage(Query query, Pageable pageable, String orderColomn, OrderTypeEnum orderType,boolean trans);

}
