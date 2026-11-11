package com.recruitment.context;

/**
 * 用户上下文（ThreadLocal存储当前用户ID和角色）
 *
 * @author Web架构技术教学组
 */
public class UserContext {

    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> roleHolder = new ThreadLocal<>();

    public static void setCurrentUser(Long userId, String role) {
        userIdHolder.set(userId);
        roleHolder.set(role);
    }

    public static Long getCurrentUserId() {
        return userIdHolder.get();
    }

    public static String getCurrentRole() {
        return roleHolder.get();
    }

    public static void clear() {
        userIdHolder.remove();
        roleHolder.remove();
    }
}
