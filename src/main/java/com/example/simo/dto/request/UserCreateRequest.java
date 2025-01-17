package com.example.simo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {


    private String userName;
    private String password;
    private String consumerKey;
    private String secretKey;

}
