package com.common.mongo;

import com.common.exception.ApplicationException;
import org.apache.commons.beanutils.MethodUtils;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * Created by Administrator on 2017/7/10.
 */
public enum QueryType {
    EQ("is"),//"="
    LT("lt"),//"<"
    LTE("lte"),//"<="
    GT("gt"),//">"
    GTE("gte"),//">="
    NE("ne"),//"!="
    LIKE("regex"),//"like"
    EXISTS("exists"),//存在
    IN("in"),// in
    ;

    QueryType(String method) {
        this.methodName = method;
    }

    private String methodName;

    public Criteria build(String key, Object value) {
        Criteria criteria = Criteria.where(key);
        try {
            return (Criteria) MethodUtils.getAccessibleMethod(Criteria.class, methodName, Object.class).invoke(criteria, value);
        } catch (Exception e) {
            throw new ApplicationException("执行查询失败", e);
        }
    }
}
