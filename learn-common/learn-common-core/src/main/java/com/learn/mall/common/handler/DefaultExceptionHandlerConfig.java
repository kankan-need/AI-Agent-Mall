package com.learn.mall.common.handler;

import com.learn.mall.common.exception.LearnMallException;
import com.learn.mall.common.response.ResponseEnum;
import com.learn.mall.common.response.ServerResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局异常处理：HTTP 状态码保持 200，错误信息通过 code 字段返回。
 */
@RestControllerAdvice
public class DefaultExceptionHandlerConfig {

    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandlerConfig.class);

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ServerResponseEntity<List<String>>> handleValidationException(Exception e) {
        logger.error("validation error", e);
        List<FieldError> fieldErrors = null;
        if (e instanceof MethodArgumentNotValidException exception) {
            fieldErrors = exception.getBindingResult().getFieldErrors();
        }
        if (e instanceof BindException exception) {
            fieldErrors = exception.getBindingResult().getFieldErrors();
        }
        if (fieldErrors == null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ServerResponseEntity.fail(ResponseEnum.METHOD_ARGUMENT_NOT_VALID));
        }
        List<String> messages = new ArrayList<>(fieldErrors.size());
        for (FieldError fieldError : fieldErrors) {
            messages.add(fieldError.getField() + ":" + fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ServerResponseEntity.fail(ResponseEnum.METHOD_ARGUMENT_NOT_VALID, messages));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ServerResponseEntity<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        logger.error("message not readable", e);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ServerResponseEntity.fail(ResponseEnum.HTTP_MESSAGE_NOT_READABLE));
    }

    @ExceptionHandler(LearnMallException.class)
    public ResponseEntity<ServerResponseEntity<Object>> handleLearnMallException(LearnMallException e) {
        logger.error("business error", e);
        if (e.getResponseEnum() != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ServerResponseEntity.fail(e.getResponseEnum(), e.getObject()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ServerResponseEntity.showFailMsg(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerResponseEntity<Void>> handleException(Exception e) {
        logger.error("unexpected error", e);
        return ResponseEntity.status(HttpStatus.OK).body(ServerResponseEntity.fail(ResponseEnum.EXCEPTION));
    }
}
