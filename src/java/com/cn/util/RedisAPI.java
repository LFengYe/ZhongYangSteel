/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * @author LFeng
 */
public class RedisAPI {
    protected static final org.slf4j.Logger LOG = LoggerFactory.getLogger(RedisAPI.class);
    private static JedisPool jedisPool = null;
    private static final String encordPassword = "Ty558168";
    
    /**
     * 构建redis连接池
     * 
     * @param ip
     * @param port
     * @return JedisPool
     */
    private static JedisPool getPool(String ip, int port) {
        if (jedisPool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
            config.setMaxActive(500);
            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
            config.setMaxIdle(5);
            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
            config.setMaxWait(1000 * 100);
            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            jedisPool = new JedisPool(config, ip, port);
        }
        return jedisPool;
    }
    
    /**
     * 返还到连接池
     * 
     * @param pool 
     * @param redis
     */
    private static void returnResource(JedisPool pool, Jedis redis) {
        if (redis != null) {
            pool.returnResource(redis);
        }
    }
    
    public static Long delKey(String key) {
        try {
            Properties prop = new Properties();
            prop.load(RedisAPI.class.getClassLoader().getResourceAsStream("./config.properties"));
            JedisPool pool = null;
            Jedis jedis = null;
            try {
                pool = getPool(prop.getProperty("REDIS_HOST"), Integer.valueOf(prop.getProperty("REDIS_PORT")));
                jedis = pool.getResource();
                return jedis.del(key);
            } catch (Exception e) {
                //释放redis对象
                if (null != pool)
                    pool.returnBrokenResource(jedis);
                LOG.error("Redis读取出错", e);
            } finally {
                //返还到连接池
                returnResource(pool, jedis);
            }
        } catch (IOException ex) {
            LOG.error("Redis配置文件读取错误", ex);
        }
        return null;
    }
    
    public static Set<String> getKeys(String pattern){
        try {
            Properties prop = new Properties();
            prop.load(RedisAPI.class.getClassLoader().getResourceAsStream("./config.properties"));
            JedisPool pool = null;
            Jedis jedis = null;
            try {
                pool = getPool(prop.getProperty("REDIS_HOST"), Integer.valueOf(prop.getProperty("REDIS_PORT")));
                jedis = pool.getResource();
                return jedis.keys(pattern);
            } catch (Exception e) {
                //释放redis对象
                if (null != pool)
                    pool.returnBrokenResource(jedis);
                LOG.error("Redis读取出错", e);
            } finally {
                //返还到连接池
                returnResource(pool, jedis);
            }
        } catch (IOException ex) {
            LOG.error("Redis配置文件读取错误", ex);
        }
        return null;
    }
    
    /**
     * 获取数据
     * 
     * @param key
     * @return
     */
    public static String get(String key){
        try {
            Properties prop = new Properties();
            prop.load(RedisAPI.class.getClassLoader().getResourceAsStream("./config.properties"));
            String value = null;
            JedisPool pool = null;
            Jedis jedis = null;
            try {
                pool = getPool(prop.getProperty("REDIS_HOST"), Integer.valueOf(prop.getProperty("REDIS_PORT")));
                jedis = pool.getResource();
                value = jedis.get(key);
            } catch (Exception e) {
                //释放redis对象
                if (null != pool)
                    pool.returnBrokenResource(jedis);
                LOG.error("Redis读取出错", e);
            } finally {
                //返还到连接池
                returnResource(pool, jedis);
            }
            return value;
        } catch (IOException ex) {
            LOG.error("Redis配置文件读取错误", ex);
        }
        return null;
    }
    
    public static String set(String key, String value) {
        try {
            Properties prop = new Properties();
            prop.load(RedisAPI.class.getClassLoader().getResourceAsStream("./config.properties"));
            JedisPool pool = null;
            Jedis jedis = null;
            try {
                pool = getPool(prop.getProperty("REDIS_HOST"), Integer.valueOf(prop.getProperty("REDIS_PORT")));
                jedis = pool.getResource();
                return jedis.set(key, value);
            } catch (Exception e) {
                //释放redis对象
                if (null != pool)
                    pool.returnBrokenResource(jedis);
                LOG.error("Redis写入出错", e);
            } finally {
                //返还到连接池
                returnResource(pool, jedis);
            }
        } catch (IOException ex) {
            LOG.error("Redis配置文件读取错误", ex);
        }
        return null;
    }
    
    public static Long setList(String key, String value) {
        try {
            Properties prop = new Properties();
            prop.load(RedisAPI.class.getClassLoader().getResourceAsStream("./config.properties"));
            JedisPool pool = null;
            Jedis jedis = null;
            try {
                pool = getPool(prop.getProperty("REDIS_HOST"), Integer.valueOf(prop.getProperty("REDIS_PORT")));
                jedis = pool.getResource();
                return jedis.rpush(key, value);
            } catch (Exception e) {
                //释放redis对象
                if (null != pool)
                    pool.returnBrokenResource(jedis);
                LOG.error("Redis写入出错", e);
            } finally {
                //返还到连接池
                returnResource(pool, jedis);
            }
        } catch (IOException ex) {
            LOG.error("Redis配置文件读取错误", ex);
        }
        return null;
    }
    
    public static String setList(String key, long index, String value) {
        try {
            Properties prop = new Properties();
            prop.load(RedisAPI.class.getClassLoader().getResourceAsStream("./config.properties"));
            JedisPool pool = null;
            Jedis jedis = null;
            try {
                pool = getPool(prop.getProperty("REDIS_HOST"), Integer.valueOf(prop.getProperty("REDIS_PORT")));
                jedis = pool.getResource();
                return jedis.lset(key, index, value);
            } catch (Exception e) {
                //释放redis对象
                if (null != pool)
                    pool.returnBrokenResource(jedis);
                LOG.error("Redis写入出错", e);
            } finally {
                //返还到连接池
                returnResource(pool, jedis);
            }
        } catch (IOException ex) {
            LOG.error("Redis配置文件读取错误", ex);
        }
        return null;
    }
    
    public static List<String> getList(String key){
        try {
            Properties prop = new Properties();
            prop.load(RedisAPI.class.getClassLoader().getResourceAsStream("./config.properties"));
            List<String> value = null;
            JedisPool pool = null;
            Jedis jedis = null;
            try {
                pool = getPool(prop.getProperty("REDIS_HOST"), Integer.valueOf(prop.getProperty("REDIS_PORT")));
                jedis = pool.getResource();
                value = jedis.lrange(key, 0, -1);
            } catch (Exception e) {
                //释放redis对象
                if (null != pool)
                    pool.returnBrokenResource(jedis);
                LOG.error("Redis读取出错", e);
            } finally {
                //返还到连接池
                returnResource(pool, jedis);
            }
            return value;
        } catch (IOException ex) {
            LOG.error("Redis配置文件读取错误", ex);
        }
        return null;
    }
    
    public static String getList(String key, long index){
        try {
            Properties prop = new Properties();
            prop.load(RedisAPI.class.getClassLoader().getResourceAsStream("./config.properties"));
            String value = null;
            JedisPool pool = null;
            Jedis jedis = null;
            try {
                pool = getPool(prop.getProperty("REDIS_HOST"), Integer.valueOf(prop.getProperty("REDIS_PORT")));
                jedis = pool.getResource();
                value = jedis.lindex(key, index);
            } catch (Exception e) {
                //释放redis对象
                if (null != pool)
                    pool.returnBrokenResource(jedis);
                LOG.error("Redis读取出错", e);
            } finally {
                //返还到连接池
                returnResource(pool, jedis);
            }
            return value;
        } catch (IOException ex) {
            LOG.error("Redis配置文件读取错误", ex);
        }
        return null;
    }
    
    public static void main(String[] args) {
        /*
        List<String> list = RedisAPI.getList("171.124.252.239");
        System.out.println(Arrays.toString(list.toArray()));
        double[] dataList = new double[list.size() - 1];
        for (int i = 1; i < list.size(); i++) {
            dataList[i - 1] = Double.valueOf(list.get(i));
        }
        System.out.println(Units.StandardDiviation(dataList));
        */
        Set<String> keys = RedisAPI.getKeys("*record");
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            System.out.println("key:" + key);
            System.out.println("value:" + Arrays.toString(RedisAPI.getList(key).toArray()));
            System.out.println("imei:" + key.substring(0, key.length() - 7));
            System.out.println("imei value:" + RedisAPI.get(key.substring(0, key.length() - 7)));
        }
    }
    
}
