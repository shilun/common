//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import com.common.exception.ApplicationException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;

abstract class MyBatisSupport {
    protected static final Logger LOGGER = Logger.getLogger(MyBatisSupport.class.getName());
    @Resource
    private SqlSessionTemplate sqlTemplate;
    @Resource
    private SqlSessionTemplate batchSqlTemplate;

    MyBatisSupport() {
    }

    protected SqlSessionTemplate getSqlTemplate(boolean batch, boolean readonly) {
        if(readonly) {
            ;
        }

        return batch?this.batchSqlTemplate:this.sqlTemplate;
    }

    protected long insert(String statement, Object parameter) {
        long res = 0L;

        try {
            if(parameter != null) {
                res = (long)this.getSqlTemplate(false, false).insert(statement, parameter);
            }

            return res;
        } catch (Exception var6) {
            LOGGER.error("add exception:" + var6.getMessage(), var6);
            throw new ApplicationException("Mybatis执行新增异常", var6);
        }
    }

    protected int delete(String statement, Object parameter) {
        boolean res = false;

        try {
            int res1 = this.getSqlTemplate(false, false).delete(statement, parameter);
            return res1;
        } catch (Exception var5) {
            LOGGER.error("查询失败：" + var5.getMessage(), var5);
            throw new ApplicationException("Mybatis执行删除异常", var5);
        }
    }

    protected int update(String statement, Object parameter) {
        int res = 0;

        try {
            if(parameter != null) {
                res = this.getSqlTemplate(false, false).update(statement, parameter);
            }

            return res;
        } catch (Exception var5) {
            LOGGER.error("Mybatis执行更新异常" + var5.getMessage(), var5);
            throw new ApplicationException("Mybatis执行更新异常", var5);
        }
    }

    protected <T> T select(String statement, Object parameter) {
        Object obj = null;

        try {
            obj = this.getSqlTemplate(false, true).selectOne(statement, parameter);
            return (T) obj;
        } catch (Exception var5) {
            LOGGER.error("Mybatis执行单条查询异常", var5);
            throw new ApplicationException("Mybatis执行单条查询异常", var5);
        }
    }

    protected <T> List<T> selectList(String statement, Object parameter) {
        List list = null;

        try {
            list = this.getSqlTemplate(false, true).selectList(statement, parameter);
            return list;
        } catch (Exception var5) {
            LOGGER.error("查询失败：" + var5.getMessage(), var5);
            throw new ApplicationException("Mybatis执行列表查询异常", var5);
        }
    }

    protected <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
        Map map = null;

        try {
            map = this.getSqlTemplate(false, true).selectMap(statement, parameter, mapKey);
            return map;
        } catch (Exception var6) {
            LOGGER.error("查询失败：" + var6.getMessage(), var6);
            throw new ApplicationException("Mybatis执行Map查询异常", var6);
        }
    }
}
