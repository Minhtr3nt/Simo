package com.example.simo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {

    private int code;
    private String message;
    private Object data;

}
