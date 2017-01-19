//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.redis;

import java.util.List;
import java.util.Set;

public interface RedisDbDao {
    void rpush(String var1, String... var2);

    void lpush(String var1, String... var2);

    Long llen(String var1);

    List lrange(String var1, int var2, int var3);

    void lset(String var1, int var2, String var3);

    String lpop(String var1);

    String rpop(String var1);

    boolean exists(String var1);

    void del(String var1);

    void set(String var1, String var2);

    String get(String var1);

    Object getBySerialize(String var1);

    void setBySerialize(String var1, Object var2);

    void setexBySerialize(String var1, int var2, Object var3);

    String getset(String var1, String var2);

    void lrem(String var1, Integer var2, String var3);

    void sadd(String var1, String var2);

    String spop(String var1);

    Set<String> smembers(String var1);

    int scard(String var1);

    void expire(String var1, int var2);

    void setex(String var1, int var2, String var3);

    void srem(String var1, String var2);

    boolean sismember(String var1, String var2);

    Long incr(String var1);
}
