package com.cuizhi.auth.exception;

import com.cuizhi.core.common.CzHttpStatus;
import com.cuizhi.core.exception.CuiZhiException;
import com.cuizhi.core.model.ResponseResult;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CuiZhiException.class)
    public ResponseResult<Void> handleCuiZhiException(CuiZhiException e) {
        return ResponseResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    public ResponseResult<Void> handleValidationException(Exception e) {
        return ResponseResult.error(CzHttpStatus.BAD_REQUEST.getCode(), CzHttpStatus.BAD_REQUEST.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseResult<Void> handleBadRequest(Exception e) {
        return ResponseResult.error(CzHttpStatus.BAD_REQUEST.getCode(), CzHttpStatus.BAD_REQUEST.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseResult<Void> handleAuthenticationException(AuthenticationException e) {
        return ResponseResult.error(CzHttpStatus.UNAUTHORIZED.getCode(), CzHttpStatus.UNAUTHORIZED.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseResult<Void> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseResult.error(CzHttpStatus.FORBIDDEN.getCode(), CzHttpStatus.FORBIDDEN.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult<Void> handleException(Exception e) {
        log.error("Unhandled exception", e);
        return ResponseResult.error(CzHttpStatus.INTERNAL_ERROR.getCode(), CzHttpStatus.INTERNAL_ERROR.getMessage());
    }
}
