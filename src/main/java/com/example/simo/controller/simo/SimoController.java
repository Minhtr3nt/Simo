package com.example.simo.controller.simo;

import com.example.simo.dto.request.CustomerAccountRequest;
import com.example.simo.dto.request.SuspectedFraudAccountRequest;
import com.example.simo.dto.request.UserAccountRequest;
import com.example.simo.dto.request.RefreshTokenRequest;
import com.example.simo.dto.response.ApiResponse;
import com.example.simo.dto.response.TokenResponse;
import com.example.simo.exception.ErrorCode;
import com.example.simo.exception.SimoException;
import com.example.simo.service.simo.IPAddressService;
import com.example.simo.service.simo.SimoService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/simo")
public class SimoController {

    private final SimoService simoService;
    private final IPAddressService ipAddressService;

    @PostMapping("/getToken")
    public ResponseEntity<ApiResponse> tokenAuthorization(@RequestHeader("Authorization") String authorizationHeader,
                                                          @RequestBody UserAccountRequest userAccountRequest,
                                                          HttpServletRequest request) {

        boolean ipCheck = ipAddressService.CheckIPAddress(request, userAccountRequest);


        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {

            String base64Credentials = authorizationHeader.substring("Basic ".length()).trim();
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
            String[] credentials = decodedString.split(":", 2);

            if (credentials.length == 2) {
                String consumerKey = credentials[0];
                String secretKey = credentials[1];
                TokenResponse token = simoService.getToken(userAccountRequest.getUserName(), userAccountRequest.getPassword(),consumerKey, secretKey);
                return ResponseEntity.ok().body(new ApiResponse(200,"Get token successful", token));
            } else {
                throw new SimoException(ErrorCode.AUTHORIZED_HEADER_INVALID);
            }

        } else {
            throw new SimoException(ErrorCode.AUTHORIZED_HEADER_INVALID);
        }
    }





    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse> refreshToken(@RequestHeader("Authorization") String authorizationHeader,
                                               @RequestBody RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {

        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {

            String base64Credentials = authorizationHeader.substring("Basic ".length()).trim();
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
            String[] credentials = decodedString.split(":", 2);

            if (credentials.length == 2) {
                String consumerKey = credentials[0];
                String secretKey = credentials[1];
                TokenResponse token = simoService.refreshToken(consumerKey, secretKey ,refreshTokenRequest);
                return ResponseEntity.ok().body(new ApiResponse(200,"Refresh token successful", token));
            } else {
                throw new SimoException(ErrorCode.AUTHORIZED_HEADER_INVALID);
            }

        } else {
            throw new SimoException(ErrorCode.AUTHORIZED_HEADER_INVALID);

        }

    }

    @PostMapping("/getListCustomerAccount")
    public ResponseEntity<ApiResponse> getListCustomerAccount(@RequestHeader("maYeuCau") String maYeuCau,
                                                              @RequestHeader("kyBaoCao") String kyBaoCao,
                                                              @RequestBody Set<CustomerAccountRequest> requests){
        ApiResponse apiResponse = simoService.collectCustomerAccount(maYeuCau, kyBaoCao,requests);
        return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping("/getListFraudAccount")
    public ResponseEntity<ApiResponse> getListFraudAccount(@RequestHeader("maYeuCau") String maYeuCau,
                                                           @RequestHeader("kyBaoCao") String kyBaoCao,
                                                           @RequestBody Set<SuspectedFraudAccountRequest> requests
                                                           ){
        ApiResponse apiResponse = simoService.collectSuspectFraudAccount(maYeuCau, kyBaoCao, requests);
        return ResponseEntity.ok().body(apiResponse);
    }

}
