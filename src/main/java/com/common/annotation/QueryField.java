package com.common.annotation;

import com.common.mongo.QueryType;

/**
 * Created by Administrator on 2017/7/10.
 */
public @interface QueryField {
    String name() default "";
    QueryType type() default QueryType.Equals;

}
