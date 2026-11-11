package com.recruitment.exception;

/**
 * 权限不足异常
 *
 * <p>由 RoleAspect 在角色不匹配时抛出，由 GlobalExceptionHandler 统一处理为 403。</p>
 *
 * @author Web架构技术教学组
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
