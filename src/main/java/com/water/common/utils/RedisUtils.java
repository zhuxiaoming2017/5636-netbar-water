package com.water.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public class RedisUtils {

    public static byte[] getByte(StringRedisTemplate stringRedisTemplate, byte[] key) {
        byte[] result = stringRedisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] value = connection.get(key);
                return value;
            }
        });
        return result;
    }

    public static byte[] getByte(StringRedisTemplate stringRedisTemplate, String key) {
        byte[] result = stringRedisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
                byte[] value = connection.get(serializer.serialize(key));
                return value;
            }
        });
        return result;
    }

    public static boolean setByte(StringRedisTemplate stringRedisTemplate, byte[] key, byte[] value) {
        boolean result = stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(key, value);
                return true;
            }
        });
        return result;
    }

    public static boolean setByte(StringRedisTemplate stringRedisTemplate, String key, String value) {
        boolean result = stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
                connection.set(serializer.serialize(key), serializer.serialize(value));
                return true;
            }
        });
        return result;
    }

    /**
     * 自动递增
     * @param stringRedisTemplate
     * @param key
     * @return
     */
    public static Long incr(StringRedisTemplate stringRedisTemplate, String key){
        String oldValue = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(oldValue)) {
            stringRedisTemplate.opsForValue().set(key, "0");
        }
        return stringRedisTemplate.opsForValue().increment(key, 1L);
    }

    /**
     * 自动递减
     * @param stringRedisTemplate
     * @param key
     * @return
     */
    public static Long decr(StringRedisTemplate stringRedisTemplate, String key){
        String oldValue = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(oldValue) || Long.parseLong(oldValue) < 1) {
            stringRedisTemplate.opsForValue().set(key, "0");
        }
        Long num = stringRedisTemplate.opsForValue().increment(key, -1L) ;
        return num < 0 ? 0 : num;
    }
}
