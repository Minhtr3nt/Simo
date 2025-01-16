package com.example.simo.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class TokenResponse {

    private String access_token;
    private String scope;
    private String token_type;
    private Date expires_in;
}
