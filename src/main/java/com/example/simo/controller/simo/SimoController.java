package com.example.simo.controller.simo;

import com.example.simo.dto.request.AccountRequest;
import com.example.simo.dto.request.RefreshTokenRequest;
import com.example.simo.dto.response.ApiResponse;
import com.example.simo.dto.response.TokenResponse;
import com.example.simo.exception.ErrorCode;
import com.example.simo.exception.SimoException;
import com.example.simo.service.simo.SimoService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/simo")
public class SimoController {

    private final SimoService simoService;


    @PostMapping("/getToken")
    public ResponseEntity<ApiResponse> decodeAuthorization(@RequestHeader("Authorization") String authorizationHeader,
                                                           @RequestBody AccountRequest accountRequest) {

        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {

            String base64Credentials = authorizationHeader.substring("Basic ".length()).trim();
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);


            String[] credentials = decodedString.split(":", 2);

            if (credentials.length == 2) {
                String consumerKey = credentials[0];
                String secretKey = credentials[1];
                TokenResponse token = simoService.getToken(accountRequest.getUserName(), accountRequest.getPassword(),consumerKey, secretKey);
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
}
