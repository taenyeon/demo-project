package com.example.demoproject.common.error.controller;

import com.example.demoproject.common.error.entity.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class RestControllerExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handelHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletResponse response) throws IOException {
        log.info("HandleHttpRequestMethodNotSupportedException", e);
        return getErrorResponse(e);
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<ErrorResponse> handelIllegalStateException(IllegalStateException e, HttpServletResponse response) throws IOException {
        log.info("HandelIllegalStateException", e);
        return getErrorResponse(e);
    }


    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        errors.put("resultCode", "401");
        errors.put("resultMessage", e.getMessage());
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(200).body(errors);
    }

    // todo 추후 실제 개발 시, Enum으로 처리 필요.
    private ResponseEntity<ErrorResponse> getErrorResponse(Exception e) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .resultCode(400)
                .resultMessage(e.getMessage())
                .build();
        return ResponseEntity.status(200).body(errorResponse);
    }
}
