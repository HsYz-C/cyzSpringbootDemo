package com.recruitment.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitment.common.Result;
import com.recruitment.context.UserContext;
import com.recruitment.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT拦截器
 *
 * @author Web架构技术教学组
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取Token
        String authHeader = request.getHeader("Authorization");

        // 2. 检查格式
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            returnUnauthorized(response, "请先登录");
            return false;
        }

        // 3. 验证Token
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            returnUnauthorized(response, "登录已过期，请重新登录");
            return false;
        }

        // 4. 存入上下文（用户ID + 角色）
        Long userId = jwtUtil.getUserIdFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);
        UserContext.setCurrentUser(userId, role);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear(); // 清除，防止内存泄漏
    }

    private void returnUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(Result.error(401, message)));
    }
}
