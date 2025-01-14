package com.example.simo.controller.simogw;

import com.example.simo.dto.request.AccountRequest;
import com.example.simo.dto.request.RefreshTokenRequest;
import com.example.simo.dto.response.ApiResponse;
import com.example.simo.service.simogw.SimoGWService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("${api.prefix}/key")
@RequiredArgsConstructor
public class SimoGWController {

    private final SimoGWService simoGWService;

    @PostMapping("/sendKey")
    public ResponseEntity<ApiResponse> sendKey(@RequestBody AccountRequest accountRequest){
       ApiResponse apiResponse =  simoGWService.sendToken(accountRequest);
       return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping("/sendRefresh")
    public ResponseEntity<ApiResponse> sendRefresh(@RequestBody RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        ApiResponse apiResponse = simoGWService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok().body(apiResponse);
    }

}
