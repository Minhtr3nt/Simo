package com.example.simo.controller.simogw;

import com.example.simo.dto.request.AccountRequest;
import com.example.simo.dto.response.ApiResponse;
import com.example.simo.service.simogw.SimoGWService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
