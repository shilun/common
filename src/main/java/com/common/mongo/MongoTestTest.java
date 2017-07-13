package com.common.mongo;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by yangyu on 2017/7/11.
 */
public class MongoTestTest extends TestCase {
    @Test
    public void test1() throws Exception {
        AbstractMongoService service = new AbstractMongoService();
        MongoTest test = new MongoTest();
//        test.setOrderColumn("name");
//        test.setId(1l);
        test.setName("sunny1");
        service.query(test);
        System.out.println("");
    }
}