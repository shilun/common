package com.common.mongo;

import com.common.annotation.QueryField;
import com.common.util.AbstractBaseEntity;

/**
 * Created by yangyu on 2017/7/11.
 */
public class MongoTest extends AbstractBaseEntity {
    @QueryField(name="",type = QueryType.LT)
    private String name;
    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void test1() {
        AbstractMongoService service = new AbstractMongoService();
        MongoTest test = new MongoTest();
        test.setName("sunny1");
        test.setAge("26");
        service.add(test);
    }
}
