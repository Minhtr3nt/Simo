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
    TOKEN_EXPIRED_REFRESH(1008, "Token is expired refresh time", HttpStatus.BAD_REQUEST),
    PASS_INVALID(1009, "Password is invalid", HttpStatus.BAD_REQUEST),
    IP_INVALID(1010, "IP is invalid", HttpStatus.BAD_REQUEST),
    IP_NOT_FOUND(1011, "IP is not found", HttpStatus.BAD_REQUEST),
    ACCOUNT_IN_USED(1012, "CustomerAccount in use by other device", HttpStatus.BAD_REQUEST),
    WEBCLIENT_FAILED(1013, "Web client fail to call",  HttpStatus.INTERNAL_SERVER_ERROR),
    NON_NULL(1014, "Don't let any field empty", HttpStatus.BAD_REQUEST),
    NOT_VALID_CHARACTERS_AMOUNT(1015, "Don't let any field empty", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1016, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    DUPLICATE_ENTRY_EXCEPTION(1017, "Your id info is duplicated", HttpStatus.BAD_REQUEST),

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
