//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import com.common.util.AbstractBaseDao;
import com.common.util.AbstractBaseEntity;
import com.common.util.MyBatisSupport;
import com.common.util.model.YesOrNoEnum;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public abstract class DefaultBaseDao<E extends AbstractBaseEntity> extends MyBatisSupport implements AbstractBaseDao<E> {
    private static final long serialVersionUID = 1L;
    private static final String ADD = "insert";
    private static final String DEL = "del";
    private static final String UP = "update";
    private static final String QUERY = "query";
    private static final String QUERY_COUNT = "queryCount";

    public DefaultBaseDao() {
    }

    public abstract String getNameSpace(String var1);

    public Long add(E entity) {
        Date createTime = new Date();
        if (entity.getCreateTime() == null) {
            entity.setCreateTime(createTime);
        }

        if (entity.getUpdateTime() == null) {
            entity.setUpdateTime(createTime);
        }

        entity.setDelStatus(YesOrNoEnum.NO.getValue());
        this.insert(this.getNameSpace("insert"), entity);
        return entity.getId();
    }

    public void up(E entity) {
        entity.setUpdateTime(new Date());
        this.update(this.getNameSpace("update"), entity);
    }

    public E findById(Long id) {
        if (id != null && id.longValue() != 0L) {
            HashMap params = new HashMap();
            params.put("id", id);
            List list = this.selectList(this.getNameSpace("query"), params);
            return list != null && list.size() > 0 ? (E) (AbstractBaseEntity) list.get(0) : null;
        } else {
            return null;
        }
    }

    public void delById(Long id) {
        HashMap params = new HashMap();
        params.put("id", id);
        this.delete(this.getNameSpace("del"), params);
    }

    public List<E> query(E query) {
        return this.selectList(this.getNameSpace("query"), query);
    }

    public E findByOne(E entity) {
        List selectList = this.selectList(this.getNameSpace("query"), entity);
        return selectList != null && selectList.size() == 1 ? (E) (AbstractBaseEntity) selectList.get(0) : null;
    }

    public int queryCount(E entity) {
        Integer select = (Integer) this.select(this.getNameSpace("queryCount"), entity);
        return select.intValue();
    }
}
