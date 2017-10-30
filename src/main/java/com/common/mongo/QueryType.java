package com.common.mongo;

/**
 * Created by Administrator on 2017/7/10.
 */
public enum QueryType {
    EQ,//"="
    LT,//"<"
    LTE,//"<="
    GT,//">"
    GTE,//">="
    NE,//"!="
    LIKE,//"like"
    EXISTS,//存在
    IN,// in
}
