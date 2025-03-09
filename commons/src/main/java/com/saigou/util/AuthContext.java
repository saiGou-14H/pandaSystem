package com.saigou.util;

public class AuthContext {
    private static final ThreadLocal<Long> id = new ThreadLocal<>();
    public static void setId(Long userId) {
        id.set(userId);
    }
    public static Long getId() {
        return id.get();
    }
    public static void clear() {
        id.remove();
    }
}
