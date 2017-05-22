//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import com.common.exception.ApplicationException;
import com.common.util.StringUtils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.MongoClientOptions.Builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class MongoDBUtil implements InitializingBean {
    private static Logger logger = Logger.getLogger(MongoDBUtil.class);
    private static MongoClient client = null;
    private List<ServerAddress> addressList = null;
    private boolean isSave = true;
    private int connectionPerHost = 50;
    private int connectionTimeout = 10000;
    private static String userName;
    private static String password;
    private static String dbName;

    public enum OrderType implements IGlossary {
        ASC("升序",1),
        Desc("降序",-1);
        OrderType(String name, Integer value) {
            this.value = value;
            this.name = name;
        }

        private Integer value;
        private String name;

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public MongoDBUtil() {
    }

    public void afterPropertiesSet() {
        boolean address = this.addressList != null && this.addressList.size() > 0;
        if (address) {
            try {
                this.initMongo();
            } catch (Exception var3) {
                logger.error("初始化mongodb链接失败!", var3);
            }
        } else {
            logger.error("没有mongodb服务地址!");
        }

    }

    public void initMongo() {
        try {
            Builder e = new Builder();
            e.connectionsPerHost(50);
            e.maxWaitTime(120000);
            e.connectTimeout(this.connectionTimeout);
            e.threadsAllowedToBlockForConnectionMultiplier(50);
            MongoClientOptions options = e.build();
            ArrayList credentialsList = new ArrayList();
            if (!StringUtils.isBlank(userName) && !StringUtils.isBlank(password)) {
                MongoCredential mc = MongoCredential.createCredential(userName, dbName, password.toCharArray());
                credentialsList.add(mc);
                client = new MongoClient(this.addressList, credentialsList, options);
            } else {
                client = new MongoClient(this.addressList, options);
            }
        } catch (Exception var5) {
            logger.error("mongodb 连接出错", var5);
        }

    }

    public static DB getTheDB() {
        return client.getDB(dbName);
    }

    public synchronized void destory() {
        client.close();
    }

    public DBCollection getCollection(String _collsName) {
        return getTheDB().getCollection(_collsName);
    }

    public boolean insert(String _collsName, Map<String, Object> map) {
        boolean flag = true;
        BasicDBObject obj = null;

        try {
            obj = mapToBson(map);
            this.getCollection(_collsName).insert(obj, WriteConcern.FSYNCED);
            return flag;
        } catch (Exception var6) {
            flag = false;
            logger.error("MongoDB插入数据错误 DBObject --> " + obj, var6);
            throw new ApplicationException("MongoDB插入数据错误:collectionName:" + _collsName + " DBObject --> " + obj, var6);
        }
    }

    public boolean insert(String _collsName, Map<String, Object> map, Boolean s) {
        boolean flag = true;
        BasicDBObject obj = null;

        try {
            obj = mapToBson(map);
            this.getCollection(_collsName).insert(obj, WriteConcern.FSYNC_SAFE);
            return flag;
        } catch (Exception var7) {
            flag = false;
            logger.error("MongoDB插入数据错误 DBObject --> " + obj, var7);
            throw new ApplicationException("MongoDB插入数据错误:collectionName:" + _collsName + " DBObject --> " + obj, var7);
        }
    }

    public int count(String _collsName, Map<String, Object> where) {
        return this.getCollection(_collsName).find(mapToBson(where)).count();
    }

    public int count(String _collsName, BasicDBObject where) {
        return this.getCollection(_collsName).find(where).count();
    }

    public boolean delete(String _collsName, Map<String, Object> map) {
        boolean flag = true;
        BasicDBObject obj = null;

        try {
            obj = mapToBson(map);
            this.getCollection(_collsName).remove(obj);
        } catch (Exception var6) {
            flag = false;
            logger.error("MongoDB删除数据错误 DBObject --> " + obj, var6);
        }

        return flag;
    }

    public boolean delete(String _collsName) {
        try {
            this.getCollection(_collsName).remove(new BasicDBObject());
        } catch (Exception var3) {
            logger.error("MongoDB删除表数据出错 " + _collsName, var3);
        }

        return true;
    }

    public boolean update(String _collsName, Map<String, Object> where, Map<String, Object> setField) {
        BasicDBObject whereObj = null;
        BasicDBObject setFieldObj = new BasicDBObject(setField);

        try {
            whereObj = mapToBson(where);
            BasicDBObject e = (new BasicDBObject()).append("$set", setFieldObj);
            this.getCollection(_collsName).update(whereObj, e, true, true);
            return true;
        } catch (Exception var7) {
            logger.error("MongoDB更新数据错误 whereObj --> " + whereObj + " setFieldObj --> " + setFieldObj, var7);
            throw new ApplicationException("MongoDB更新数据错误 whereObj --> " + whereObj + " setFieldObj --> " + setFieldObj, var7);
        }
    }

    public boolean morgeUpdate(String _collsName, Map<String, Object> where, Map<String, Object> setField) {
        BasicDBObject whereObj = null;
        BasicDBObject setFieldObj = null;

        try {
            whereObj = mapToBson(where);
            setFieldObj = mapToBson(setField);
            this.getCollection(_collsName).update(whereObj, setFieldObj);
            return true;
        } catch (Exception var7) {
            logger.error("MongoDB更新数据错误 whereObj --> " + whereObj + " setFieldObj --> " + setFieldObj, var7);
            throw new ApplicationException("MongoDB更新数据错误 whereObj --> " + whereObj + " setFieldObj --> " + setFieldObj, var7);
        }
    }

    public boolean findAndModify(String _collsName, Map<String, Object> where, Map<String, Object> setField) {
        BasicDBObject whereObj = null;
        BasicDBObject setFieldObj = null;

        try {
            whereObj = mapToBson(where);
            setFieldObj = mapToBson(setField);
            this.getCollection(_collsName).findAndModify(whereObj, (DBObject) null, (DBObject) null, false, setFieldObj, false, true);
            return true;
        } catch (Exception var7) {
            logger.error("MongoDB更新数据错误 whereObj --> " + whereObj + " setFieldObj --> " + setFieldObj, var7);
            throw new ApplicationException("MongoDB更新数据错误 whereObj --> " + whereObj + " setFieldObj --> " + setFieldObj, var7);
        }
    }

    public Map<String, Object> findOne(String _collsName, Map<String, Object> where) {
        BasicDBObject whereObj = mapToBson(where);
        BasicDBObject result = null;
        Iterator iterator = this.getCollection(_collsName).find(whereObj).iterator();
        if (iterator.hasNext()) {
            result = (BasicDBObject) iterator.next();
        }

        return (Map) (result != null ? bsonTomap(result) : result);
    }

    public List<Map<String, Object>> findList(String _collsName, Map<String, Object> where) {
        ArrayList list = new ArrayList();
        BasicDBObject whereObj = mapToBson(where);
        Iterator iterator = null;
        BasicDBObject next;
        if (whereObj == null) {
            iterator = this.getCollection(_collsName).find().iterator();

            while (iterator.hasNext()) {
                next = (BasicDBObject) iterator.next();
                list.add(bsonTomap(next));
            }

            return list;
        } else {
            iterator = this.getCollection(_collsName).find(whereObj).iterator();

            while (iterator.hasNext()) {
                next = (BasicDBObject) iterator.next();
                list.add(bsonTomap(next));
            }

            return list;
        }
    }

    public List<Map<String, Object>> findListBySort(String _collsName, Map<String, Object> where, List<String> sortName, Integer sortType) {
        ArrayList list = new ArrayList();
        BasicDBObject whereObj = mapToBson(where);
        Iterator iterator = null;
        if (null == sortType) {
            sortType = Integer.valueOf(1);
        }

        if (null != sortName && sortName.size() >= 0) {
            HashMap sortMap = new HashMap();
            Iterator sortDB = sortName.iterator();

            while (sortDB.hasNext()) {
                String next = (String) sortDB.next();
                sortMap.put(next, sortType);
            }

            BasicDBObject sortDB1 = new BasicDBObject(sortMap);
            BasicDBObject next1;
            if (whereObj == null) {
                iterator = this.getCollection(_collsName).find().sort(sortDB1).iterator();

                while (iterator.hasNext()) {
                    next1 = (BasicDBObject) iterator.next();
                    list.add(bsonTomap(next1));
                }

                return list;
            } else {
                iterator = this.getCollection(_collsName).find(whereObj).sort(sortDB1).iterator();

                while (iterator.hasNext()) {
                    next1 = (BasicDBObject) iterator.next();
                    list.add(bsonTomap(next1));
                }

                return list;
            }
        } else {
            throw new ApplicationException("排序字段不能为null");
        }
    }

    public List<Map<String, Object>> query(String _collsName, Map<String, Object> where) {
        Integer pageSize = Integer.valueOf(1000);
        ArrayList list = new ArrayList();
        BasicDBObject whereObj = mapToBson(where);
        Iterator iterator = null;
        BasicDBObject next;
        if (whereObj == null) {
            iterator = this.getCollection(_collsName).find().limit(pageSize.intValue()).iterator();

            do {
                if (!iterator.hasNext()) {
                    return list;
                }

                next = (BasicDBObject) iterator.next();
                list.add(bsonTomap(next));
            } while (list.size() <= pageSize.intValue());

            throw new ApplicationException("业务数据过大必需通过分页程序来实现查询");
        } else {
            iterator = this.getCollection(_collsName).find(whereObj).limit(pageSize.intValue()).iterator();

            while (iterator.hasNext()) {
                next = (BasicDBObject) iterator.next();
                list.add(bsonTomap(next));
            }

            return list;
        }
    }

    public Page<Map<String, Object>> queryPage(String _collsName, Map<String, Object> where, Pageable page, String orderBy) {
        int count = this.count(_collsName, where);
        int skip = page.getPageSize() * page.getPageNumber();
        ArrayList listdata = new ArrayList(page.getPageSize());
        BasicDBObject sort = new BasicDBObject(orderBy, Integer.valueOf(1));
        Iterator iterator = this.getCollection(_collsName).find(mapToBson(where)).sort(sort).skip(skip).limit(page.getPageSize()).iterator();

        while (iterator.hasNext()) {
            BasicDBObject pageData = (BasicDBObject) iterator.next();
            listdata.add(bsonTomap(pageData));
        }

        PageImpl pageData1 = new PageImpl(listdata, page, (long) count);
        return pageData1;
    }

    public Page<Map<String, Object>> queryPage(String _collsName, Map<String, Object> where, Pageable page, String orderBy,OrderType type) {
        int count = this.count(_collsName, where);
        int skip = page.getPageSize() * page.getPageNumber();
        ArrayList listdata = new ArrayList(page.getPageSize());
        BasicDBObject sort = new BasicDBObject(orderBy, type.getValue());
        Iterator iterator = this.getCollection(_collsName).find(mapToBson(where)).sort(sort).skip(skip).limit(page.getPageSize()).iterator();

        while (iterator.hasNext()) {
            BasicDBObject pageData = (BasicDBObject) iterator.next();
            listdata.add(bsonTomap(pageData));
        }

        PageImpl pageData1 = new PageImpl(listdata, page, (long) count);
        return pageData1;
    }

    public DBObject queryFileDataById(String dbCollectionName, String id) {
        try {
            DBCollection e = getTheDB().getCollection(dbCollectionName);
            BasicDBObject object = new BasicDBObject("_id", new ObjectId(id));
            return e.findOne(object);
        } catch (IllegalArgumentException var5) {
            throw new ApplicationException("查询mongodb 异常 collectionName:" + dbCollectionName, var5);
        }
    }

    public static BasicDBObject mapToBson(Map<String, Object> map) {
        BasicDBObject obj = new BasicDBObject();
        Iterator entries = map.entrySet().iterator();

        while (true) {
            while (true) {
                Entry entry;
                do {
                    if (!entries.hasNext()) {
                        return obj;
                    }

                    entry = (Entry) entries.next();
                } while (entry.getValue() == null);

                if (entry.getValue().getClass().isArray() && !(entry.getValue() instanceof byte[])) {
                    BasicDBList values = new BasicDBList();
                    Object[] array = (Object[]) ((Object[]) entry.getValue());
                    Object[] var6 = array;
                    int var7 = array.length;

                    for (int var8 = 0; var8 < var7; ++var8) {
                        Object o = var6[var8];
                        values.add(o);
                    }

                    obj.append((String) entry.getKey(), new BasicDBObject("$in", values));
                } else {
                    obj.append((String) entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public static Map<String, Object> bsonTomap(BasicDBObject doc) {
        HashMap result = new HashMap();
        Iterator iterator = doc.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public void delFileData(String dbCollectionName, String mongodbId) throws Exception {
        BasicDBObject object = new BasicDBObject();
        object.put("_id", new ObjectId(mongodbId));
        getTheDB().getCollection(dbCollectionName).remove(object);
    }

    public long queryTotal(String _collsName, Map<String, Object> where) {
        return (long) this.getCollection(_collsName).find(mapToBson(where)).count();
    }

    private void isError(String error) throws Exception {
        if (error != null) {
            throw new Exception(error);
        }
    }

    public void setDbName(String dbName) {
        MongoDBUtil.dbName = dbName;
    }

    public void setAddressList(List<ServerAddress> addressList) {
        this.addressList = addressList;
    }

    public void setIsSave(boolean isSave) {
        this.isSave = isSave;
    }

    public void setConnectionPerHost(int connectionPerHost) {
        this.connectionPerHost = connectionPerHost;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setUserName(String userName) {
        MongoDBUtil.userName = userName;
    }

    public void setPassword(String password) {
        MongoDBUtil.password = password;
    }
}
