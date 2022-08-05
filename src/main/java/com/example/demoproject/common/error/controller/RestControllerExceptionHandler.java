package com.example.demoproject.common.error.controller;

import com.example.demoproject.common.error.entity.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
@Slf4j
public class RestControllerExceptionHandler {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handelHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletResponse response) throws IOException {
        log.info("HandleHttpRequestMethodNotSupportedException", e);
        return getErrorResponse(response, e);
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<ErrorResponse> handelIllegalArgumentException(IllegalStateException e, HttpServletResponse response) throws IOException {
        log.info("HandelIllegalStateException", e);
        return getErrorResponse(response, e);
    }

    // todo 추후 실제 개발 시, Enum으로 처리 필요.
    private ResponseEntity<ErrorResponse> getErrorResponse(HttpServletResponse response, Exception e) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(400)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(200).body(errorResponse);
    }
}
