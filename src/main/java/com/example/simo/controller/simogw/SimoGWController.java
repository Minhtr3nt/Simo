package com.example.simo.controller.simogw;

import com.example.simo.dto.request.CustomerAccountRequest;
import com.example.simo.dto.request.SuspectedFraudAccountRequest;
import com.example.simo.dto.request.UserAccountRequest;
import com.example.simo.dto.request.RefreshTokenRequest;
import com.example.simo.dto.response.ApiResponse;
import com.example.simo.service.simogw.SimoGWService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("${api.prefix}/simogw")
@RequiredArgsConstructor
public class SimoGWController {

    private final SimoGWService simoGWService;

    @PostMapping("/sendKey")
    public ResponseEntity<ApiResponse> sendKey(@RequestBody UserAccountRequest userAccountRequest){
       ApiResponse apiResponse =  simoGWService.sendToken(userAccountRequest);
       return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping("/sendRefresh")
    public ResponseEntity<ApiResponse> sendRefresh(@RequestBody RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        ApiResponse apiResponse = simoGWService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping("/collectCustomerAccount")
    public ResponseEntity<ApiResponse> collectCusAccount(@RequestHeader("maYeuCau") String maYeuCau,
                                                         @RequestHeader("kyBaoCao") String kyBaoCao,
                                                         @RequestBody Set<CustomerAccountRequest> request){
        ApiResponse apiResponse = simoGWService.getCustomerAccountList(maYeuCau, kyBaoCao, request);
        return ResponseEntity.ok().body(apiResponse);

    }

    @PostMapping("/collectCustomerFraudAccount")
    public ResponseEntity<ApiResponse> collectCusFraudAccount(@RequestHeader("maYeuCau") String maYeuCau,
                                                         @RequestHeader("kyBaoCao") String kyBaoCao,
                                                         @RequestBody Set<SuspectedFraudAccountRequest> request){
        ApiResponse apiResponse = simoGWService.getSuspectFraudAccountList(maYeuCau, kyBaoCao, request);
        return ResponseEntity.ok().body(apiResponse);

    }

}
