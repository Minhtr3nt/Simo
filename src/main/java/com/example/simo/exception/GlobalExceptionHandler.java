package com.example.simo.exception;

import com.example.simo.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = SimoException.class)
    public ResponseEntity<ApiResponse> simoException(SimoException simoException){
        ErrorCode errorCode =simoException.getErrorCode();
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(new ApiResponse(errorCode.getCode(), errorCode.getMessage(), null));

    }
}
