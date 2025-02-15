package com.saigou.util;

import cn.hutool.db.nosql.redis.RedisDS;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
@Component
public class
RedisUtil {

    private static Jedis jedis;

    RedisUtil() {
        jedis = RedisDS.create().getJedis();
    }
    public static void set(String key, String value) {
        jedis.set(key, value);
    }

    public static String get(String key) {
        return jedis.get(key);
    }

    public static long del(String key) {
        System.out.println("删除了" + jedis.del(key) + "个键");
        return jedis.del(key);
    }

    public static void set(String key, String value, long expire) {
        jedis.setex(key, expire, value);
    }

    public static void main(String[] args) {
        set("key", "value", 10);
        System.out.println(get("key"));
        del("key");  // 调用 del 方法并处理返回值
    }
}
