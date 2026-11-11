package com.recruitment.exception;

import com.recruitment.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author Web架构技术教学组
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 权限不足：HTTP 403 + body code 403，与拦截器的 401 保持一致
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Result<Void>> handleForbiddenException(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Result.error(403, e.getMessage()));
    }
}
