package com.common.mongo;

import com.common.util.AbstractBaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */
public interface MongoService<T extends AbstractBaseEntity> {
    /***
     * 添加或保存
     * @param entity
     * @return
     */
    public Long save(T entity);

    /***
     * 根据id查询
     * @param id
     * @return
     */
    public T findById(Long id);

    /***
     * 根据条件查询单条记录
     * @param entity
     * @return
     */
    public T findByOne(T entity);

    /**
     * 根据id 删除
     * @param id
     */
    public void delById(Long id);

    /**
     * 模糊查询
     * @param entity
     * @return
     */
    public List<T> query(T entity);

    /**
     * 查询总条数
     * @param entity
     * @return
     */
    public Long queryCount(T entity);

    /***
     * 分页查询
     * @param entity
     * @param pageable
     * @return
     */
    public Page<T> queryByPage(T entity, Pageable pageable);


    /**
     * 分页查询
     * @param query
     * @param pageable
     * @return
     */
    public Page<T> queryByPage(Query query,Pageable pageable);

}
