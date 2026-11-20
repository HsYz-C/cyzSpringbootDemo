package com.recruitment.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * 封装常用的Redis操作
 *
 * <p>所有操作都做了异常捕获：Redis 不可用时自动降级（读返回 null、写忽略），
 * 业务代码会回退到数据库查询，不会因为缓存故障导致接口 500。</p>
 *
 * @author Web架构技术教学组
 */
@Component
public class RedisUtil {

    private static final Logger log = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置缓存
     */
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.warn("Redis写入失败，已忽略 key={}，原因：{}", key, e.getMessage());
        }
    }

    /**
     * 设置缓存（带过期时间）
     *
     * @param timeout 过期时间（秒）
     */
    public void set(String key, Object value, long timeout) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("Redis写入失败，已忽略 key={}，原因：{}", key, e.getMessage());
        }
    }

    /**
     * 获取缓存
     */
    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.warn("Redis读取失败，回退数据库 key={}，原因：{}", key, e.getMessage());
            return null;
        }
    }

    /**
     * 删除缓存
     */
    public Boolean delete(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            log.warn("Redis删除失败 key={}，原因：{}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 按前缀/通配符批量删除缓存
     */
    public void deleteByPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            log.warn("Redis批量删除失败 pattern={}，原因：{}", pattern, e.getMessage());
        }
    }

    /**
     * 判断key是否存在
     */
    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.warn("Redis查询key失败 key={}，原因：{}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 设置过期时间（秒）
     */
    public Boolean expire(String key, long timeout) {
        try {
            return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("Redis设置过期时间失败 key={}，原因：{}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 获取过期时间（秒）
     */
    public Long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("Redis获取过期时间失败 key={}，原因：{}", key, e.getMessage());
            return -1L;
        }
    }
}
