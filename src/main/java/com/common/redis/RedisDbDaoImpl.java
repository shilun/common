//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.redis;

import com.common.redis.RedisDbDao;
import com.thoughtworks.xstream.XStream;
import java.util.List;
import java.util.Set;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisDbDaoImpl implements RedisDbDao {
    private ShardedJedisPool shardedJedisPool;
    private boolean use;
    private static final int DEFAULTEXPIRETIME = 10800;

    public RedisDbDaoImpl() {
    }

    public void rpush(String key, String... val) {
        ShardedJedis shardedJedis = null;

        try {
            shardedJedis = this.shardedJedisPool.getResource();
            shardedJedis.rpush(key, val);
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        } finally {
            if(shardedJedis != null) {
                this.shardedJedisPool.returnBrokenResource(shardedJedis);
            }

        }

    }

    public void lpush(String key, String... val) {
        ShardedJedis shardedJedis = null;

        try {
            shardedJedis = this.shardedJedisPool.getResource();
            shardedJedis.lpush(key, val);
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        } finally {
            if(shardedJedis != null) {
                this.shardedJedisPool.returnBrokenResource(shardedJedis);
            }

        }

    }



    public Long llen(String key) {
        if(!this.use) {
            return null;
        } else {
            ShardedJedis shardedJedis = null;

            Long e;
            try {
                shardedJedis = this.shardedJedisPool.getResource();
                e = shardedJedis.llen(key);
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            } finally {
                if(shardedJedis != null) {
                    this.shardedJedisPool.returnBrokenResource(shardedJedis);
                }

            }

            return e;
        }
    }

    public List lrange(String key, int start, int end) {
        if(!this.use) {
            return null;
        } else {
            ShardedJedis shardedJedis = null;

            List e;
            try {
                shardedJedis = this.shardedJedisPool.getResource();
                e = shardedJedis.lrange(key, (long)start, (long)end);
            } catch (Exception var9) {
                throw new RuntimeException(var9);
            } finally {
                if(shardedJedis != null) {
                    this.shardedJedisPool.returnBrokenResource(shardedJedis);
                }

            }

            return e;
        }
    }

    public void lset(String key, int index, String value) {
        ShardedJedis shardedJedis = null;

        try {
            shardedJedis = this.shardedJedisPool.getResource();
            shardedJedis.lset(key, (long)index, value);
        } catch (Exception var9) {
            throw new RuntimeException(var9);
        } finally {
            if(shardedJedis != null) {
                this.shardedJedisPool.returnBrokenResource(shardedJedis);
            }

        }

    }

    public String lpop(String key) {
        if(!this.use) {
            return null;
        } else {
            ShardedJedis shardedJedis = null;

            String e;
            try {
                shardedJedis = this.shardedJedisPool.getResource();
                e = shardedJedis.lpop(key);
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            } finally {
                if(shardedJedis != null) {
                    this.shardedJedisPool.returnBrokenResource(shardedJedis);
                }

            }

            return e;
        }
    }

    public String rpop(String key) {
        if(!this.use) {
            return null;
        } else {
            ShardedJedis shardedJedis = null;

            String e;
            try {
                shardedJedis = this.shardedJedisPool.getResource();
                e = shardedJedis.rpop(key);
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            } finally {
                if(shardedJedis != null) {
                    this.shardedJedisPool.returnBrokenResource(shardedJedis);
                }

            }

            return e;
        }
    }

    public boolean exists(String key) {
        ShardedJedis shardedJedis = null;

        boolean e;
        try {
            shardedJedis = this.shardedJedisPool.getResource();
            e = shardedJedis.exists(key).booleanValue();
        } catch (Exception var7) {
            throw new RuntimeException(var7);
        } finally {
            if(shardedJedis != null) {
                this.shardedJedisPool.returnBrokenResource(shardedJedis);
            }

        }

        return e;
    }

    public void del(String key) {
        ShardedJedis shardedJedis = null;

        try {
            shardedJedis = this.shardedJedisPool.getResource();
            shardedJedis.del(key);
        } catch (Exception var7) {
            throw new RuntimeException(var7);
        } finally {
            if(shardedJedis != null) {
                this.shardedJedisPool.returnBrokenResource(shardedJedis);
            }

        }

    }

    public void set(String key, String val) {
        ShardedJedis shardedJedis = null;

        try {
            shardedJedis = this.shardedJedisPool.getResource();
            shardedJedis.setex(key, 10800, val);
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        } finally {
            if(shardedJedis != null) {
                this.shardedJedisPool.returnBrokenResource(shardedJedis);
            }

        }

    }

    public String get(String key) {
        if(!this.use) {
            return null;
        } else {
            ShardedJedis shardedJedis = null;

            String e;
            try {
                shardedJedis = this.shardedJedisPool.getResource();
                e = shardedJedis.get(key);
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            } finally {
                if(shardedJedis != null) {
                    this.shardedJedisPool.returnBrokenResource(shardedJedis);
                }

            }

            return e;
        }
    }

    public String getset(String key, String value) {
        if(!this.use) {
            return null;
        } else {
            ShardedJedis shardedJedis = null;

            String e;
            try {
                shardedJedis = this.shardedJedisPool.getResource();
                e = shardedJedis.getSet(key, value);
            } catch (Exception var8) {
                throw new RuntimeException(var8);
            } finally {
                if(shardedJedis != null) {
                    this.shardedJedisPool.returnBrokenResource(shardedJedis);
                }

            }

            return e;
        }
    }

    public void lrem(String key, Integer count, String val) {
        ShardedJedis shardedJedis = null;

        try {
            shardedJedis = this.shardedJedisPool.getResource();
            shardedJedis.lrem(key, (long)count.intValue(), val);
        } catch (Exception var9) {
            throw new RuntimeException(var9);
        } finally {
            if(shardedJedis != null) {
                this.shardedJedisPool.returnBrokenResource(shardedJedis);
            }

        }

    }

    public void sadd(String key, String val) {
        ShardedJedis shardedJedis = null;

        try {
            shardedJedis = this.shardedJedisPool.getResource();
            shardedJedis.sadd(key, new String[]{val});
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        } finally {
            if(shardedJedis != null) {
                this.shardedJedisPool.returnBrokenResource(shardedJedis);
            }

        }

    }

    public String spop(String key) {
        if(!this.use) {
            return null;
        } else {
            ShardedJedis shardedJedis = null;

            String e;
            try {
                shardedJedis = this.shardedJedisPool.getResource();
                e = shardedJedis.spop(key);
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            } finally {
                if(shardedJedis != null) {
                    this.shardedJedisPool.returnBrokenResource(shardedJedis);
                }

            }

            return e;
        }
    }

    public Set<String> smembers(String key) {
        if(!this.use) {
            return null;
        } else {
            ShardedJedis shardedJedis = null;

            Set e;
            try {
                shardedJedis = this.shardedJedisPool.getResource();
                e = shardedJedis.smembers(key);
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            } finally {
                if(shardedJedis != null) {
                    this.shardedJedisPool.returnBrokenResource(shardedJedis);
                }

            }

            return e;
        }
    }

    public int scard(String key) {
        if(!this.use) {
            return 0;
        } else {
            ShardedJedis shardedJedis = null;

            int var4;
            try {
                shardedJedis = this.shardedJedisPool.getResource();
                Long e = shardedJedis.scard(key);
                var4 = e.intValue();
            } catch (Exception var8) {
                throw new RuntimeException(var8);
            } finally {
                if(shardedJedis != null) {
                    this.shardedJedisPool.returnBrokenResource(shardedJedis);
                }

            }

            return var4;
        }
    }

    public void expire(String key, int num) {
        ShardedJedis shardedJedis = null;

        try {
            shardedJedis = this.shardedJedisPool.getResource();
            shardedJedis.expire(key, num);
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        } finally {
            if(shardedJedis != null) {
                this.shardedJedisPool.returnBrokenResource(shardedJedis);
            }

        }

    }

    public void setex(String key, int seconds, String value) {
        ShardedJedis shardedJedis = null;

        try {
            shardedJedis = this.shardedJedisPool.getResource();
            shardedJedis.setex(key, seconds, value);
        } catch (Exception var9) {
            throw new RuntimeException(var9);
        } finally {
            if(shardedJedis != null) {
                this.shardedJedisPool.returnBrokenResource(shardedJedis);
            }

        }

    }

    public void srem(String key, String val) {
        ShardedJedis shardedJedis = null;

        try {
            shardedJedis = this.shardedJedisPool.getResource();
            shardedJedis.srem(key, new String[]{val});
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        } finally {
            if(shardedJedis != null) {
                this.shardedJedisPool.returnBrokenResource(shardedJedis);
            }

        }

    }

    public boolean sismember(String key, String member) {
        if(!this.use) {
            return false;
        } else {
            ShardedJedis shardedJedis = null;

            boolean e;
            try {
                shardedJedis = this.shardedJedisPool.getResource();
                e = shardedJedis.sismember(key, member).booleanValue();
            } catch (Exception var8) {
                throw new RuntimeException(var8);
            } finally {
                if(shardedJedis != null) {
                    this.shardedJedisPool.returnBrokenResource(shardedJedis);
                }

            }

            return e;
        }
    }

    public Long incr(String key) {
        if(!this.use) {
            return null;
        } else {
            ShardedJedis shardedJedis = null;

            Long e;
            try {
                shardedJedis = this.shardedJedisPool.getResource();
                e = shardedJedis.incr(key);
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            } finally {
                if(shardedJedis != null) {
                    this.shardedJedisPool.returnBrokenResource(shardedJedis);
                }

            }

            return e;
        }
    }

    public boolean isUse() {
        return this.use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    public Object getBySerialize(String key) {
        if(!this.use) {
            return null;
        } else {
            ShardedJedis shardedJedis = null;

            Object var5;
            try {
                shardedJedis = this.shardedJedisPool.getResource();
                String e = shardedJedis.get(key);
                XStream xs = new XStream();
                if(e != null) {
                    var5 = xs.fromXML(e);
                    return var5;
                }

                var5 = null;
            } catch (Exception var9) {
                throw new RuntimeException(var9);
            } finally {
                if(shardedJedis != null) {
                    this.shardedJedisPool.returnBrokenResource(shardedJedis);
                }

            }

            return var5;
        }
    }

    public void setBySerialize(String key, Object val) {
        ShardedJedis shardedJedis = null;

        try {
            shardedJedis = this.shardedJedisPool.getResource();
            XStream e = new XStream();
            if(val != null) {
                shardedJedis.setex(key, 10800, e.toXML(val));
                return;
            }
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        } finally {
            if(shardedJedis != null) {
                this.shardedJedisPool.returnBrokenResource(shardedJedis);
            }

        }

    }

    public void setexBySerialize(String key, int seconds, Object val) {
        ShardedJedis shardedJedis = null;

        try {
            shardedJedis = this.shardedJedisPool.getResource();
            XStream e = new XStream();
            if(val != null) {
                shardedJedis.setex(key, seconds, e.toXML(val));
                return;
            }
        } catch (Exception var9) {
            throw new RuntimeException(var9);
        } finally {
            if(shardedJedis != null) {
                this.shardedJedisPool.returnBrokenResource(shardedJedis);
            }

        }

    }

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }
}
