package com.example.simo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountRequest {
    private String grant_type;
    private String userName;
    private String password;
}
