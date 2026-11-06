package com.recruitment.context;

/**
 * 用户上下文（ThreadLocal存储当前用户ID）
 *
 * @author Web架构技术教学组
 */
public class UserContext {

    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

    public static void setCurrentUserId(Long userId) {
        userIdHolder.set(userId);
    }

    public static Long getCurrentUserId() {
        return userIdHolder.get();
    }

    public static void clear() {
        userIdHolder.remove();
    }
}
