//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import com.common.exception.BizException;
import com.common.redis.RedisDbDao;
import com.common.util.AbstractBaseDao;
import com.common.util.AbstractBaseEntity;
import com.common.util.AbstractBaseInterface;
import com.common.util.AbstractBaseService;
import com.common.util.model.YesOrNoEnum;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public abstract class DefaultBaseService<E extends AbstractBaseEntity> extends AbstractBaseInterface implements AbstractBaseService<E> {
    private static final long serialVersionUID = 1L;
    protected RedisDbDao redisDbDao;

    public DefaultBaseService() {
    }

    public abstract AbstractBaseDao<E> getBaseDao();

    public void setRedisDbDao(RedisDbDao redisDbDao) {
        this.redisDbDao = redisDbDao;
    }

    public Long add(E entity) {
        if(entity == null) {
            throw new BizException("add entity error:entity is null");
        } else {
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            return this.getBaseDao().add(entity);
        }
    }

    public void up(E entity) {
        if(entity == null) {
            throw new BizException("up entity error:entity is null");
        } else {
            entity.setUpdateTime(new Date());
            this.getBaseDao().up(entity);
        }
    }

    public void save(E entity) {
        if(entity == null) {
            throw new BizException("doSave error:entity is null");
        } else {
            if(entity.getId() == null) {
                this.add(entity);
            } else {
                this.up(entity);
            }

        }
    }

    public E findById(Long id) {
        return this.getBaseDao().findById(id);
    }

    public void delById(Long id) {
        this.getBaseDao().delById(id);
    }

    public List<E> query(E entity) {
        return this.getBaseDao().query(entity);
    }

    public int queryCount(E entity) {
        return this.getBaseDao().queryCount(entity);
    }

    public Page<E> queryByPage(E entity, Pageable page) {
        entity.setDelStatus(YesOrNoEnum.NO.getValue());
        List listdata = null;
        int count = this.queryCount(entity);
        if(count > page.getPageSize()) {
            entity.setStartRow(Integer.valueOf(page.getPageSize() * (page.getPageNumber() + 1) - page.getPageSize()));
            entity.setEndRow(Integer.valueOf(page.getPageSize()));
        }

        listdata = this.query(entity);
        PageImpl pageData = new PageImpl(listdata, page, (long)count);
        return pageData;
    }

    public E findByOne(E entity) {
        return this.getBaseDao().findByOne(entity);
    }
}
