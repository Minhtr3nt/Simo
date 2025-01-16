package com.example.simo.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimoException extends RuntimeException{

    public SimoException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    private ErrorCode errorCode;
}
