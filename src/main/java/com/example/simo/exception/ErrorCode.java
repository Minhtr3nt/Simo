package com.example.simo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    AUTHORIZED_HEADER_INVALID(1001, "Authorized header is failed", HttpStatus.BAD_REQUEST),
    KEY_INVALID(1002, "Key is invalid", HttpStatus.BAD_REQUEST),
    USER_INVALID(1003, "User is invalid", HttpStatus.BAD_REQUEST),
    TOKEN_INVALID(1004, "Token is invalid", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1005, "Token is expired, please create a new one", HttpStatus.BAD_REQUEST),
    PASS_INVALID(1006, "Password is invalid", HttpStatus.BAD_REQUEST),
    IP_INVALID(1007, "IP not found", HttpStatus.BAD_REQUEST),
    ACCOUNT_IN_USED(1008, "Account in use", HttpStatus.BAD_REQUEST)
    ;
    ErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
    private int code;
    private String message;
    private HttpStatusCode statusCode;


}
