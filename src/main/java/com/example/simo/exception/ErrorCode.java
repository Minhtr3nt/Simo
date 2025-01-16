package com.example.simo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    AUTHORIZED_HEADER_INVALID(1001, "Authorized header is failed", HttpStatus.BAD_REQUEST),
    KEY_INVALID(1002, "Key is invalid", HttpStatus.BAD_REQUEST),
    USER_INVALID(1003, "User is invalid", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1004, "User is invalid", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_FOUND(1005, "Token is not found", HttpStatus.BAD_REQUEST),
    TOKEN_INVALID(1006, "Token is invalid", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1007, "Token is expired, please create a new one", HttpStatus.BAD_REQUEST),
    PASS_INVALID(1008, "Password is invalid", HttpStatus.BAD_REQUEST),
    IP_INVALID(1009, "IP is invalid", HttpStatus.BAD_REQUEST),
    IP_NOT_FOUND(1010, "IP is not found", HttpStatus.BAD_REQUEST),
    ACCOUNT_IN_USED(1011, "Account in use by other device", HttpStatus.BAD_REQUEST),
    WEBCLIENT_FAILED(1012, "Web client fail to call",  HttpStatus.INTERNAL_SERVER_ERROR);
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
