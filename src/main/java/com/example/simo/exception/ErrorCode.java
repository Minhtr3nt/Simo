package com.example.simo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {


    private int code;
    private String message;
    private HttpStatus status;

    public ErrorCode(int code, String message)
}
