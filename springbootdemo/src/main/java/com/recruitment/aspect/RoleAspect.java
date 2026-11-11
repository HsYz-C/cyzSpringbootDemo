package com.recruitment.aspect;

import com.recruitment.annotation.RequireRole;
import com.recruitment.context.UserContext;
import com.recruitment.exception.ForbiddenException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 角色权限切面
 *
 * <p>方法执行前检查 @RequireRole 要求的角色，不匹配则抛 ForbiddenException。</p>
 *
 * @author Web架构技术教学组
 */
@Aspect
@Component
public class RoleAspect {

    @Before("@annotation(com.recruitment.annotation.RequireRole)")
    public void checkRole(JoinPoint jp) {
        // 取方法上的注解
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        RequireRole requireRole = method.getAnnotation(RequireRole.class);
        String required = requireRole.value();

        // 取当前用户角色
        String currentRole = UserContext.getCurrentRole();

        // ADMIN 拥有所有权限
        if ("ADMIN".equals(currentRole)) {
            return;
        }

        // 角色不匹配
        if (!required.equals(currentRole)) {
            throw new ForbiddenException("权限不足，需要角色：" + required);
        }
    }
}
