package com.example.simo.exception;

import com.example.simo.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = SimoException.class)
    public ResponseEntity<ApiResponse> simoExceptionHandler(SimoException simoException){

        ErrorCode errorCode =simoException.getErrorCode();
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(new ApiResponse(errorCode.getCode(), errorCode.getMessage(), null));

    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ApiResponse> duplicateEntryNaturalIDHandler(
            SQLIntegrityConstraintViolationException exception){
        ErrorCode errorCode = ErrorCode.DUPLICATE_ENTRY_EXCEPTION;
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(new ApiResponse(errorCode.getCode(), errorCode.getMessage(), null));

    }

}
