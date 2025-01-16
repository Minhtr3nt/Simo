package com.example.simo.service.simogw;

import com.example.simo.dto.request.AccountRequest;
import com.example.simo.dto.request.RefreshTokenRequest;
import com.example.simo.dto.response.ApiResponse;
import com.example.simo.exception.ErrorCode;
import com.example.simo.exception.SimoException;
import com.example.simo.model.User;
import com.example.simo.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


import java.text.ParseException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class SimoGWService {

    private final WebClient webClient;
    private final UserRepository userRepository;

    public ApiResponse sendToken(AccountRequest request) {
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(()-> new SimoException(ErrorCode.USER_NOT_FOUND));;
        String base = user.getConsumerKey() + ":" + user.getSecretKey();
        String base64Encode = Base64.getEncoder().encodeToString(base.getBytes());

        String apiUrl = "http://localhost:8085/api/v2/simo/getToken";
        AccountRequest requestPayload = new AccountRequest(request.getGrant_type(), request.getUserName(),
                request.getPassword());

        try{
            return webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Basic " + base64Encode)
                    .body(Mono.just(requestPayload), AccountRequest.class)
                    .retrieve()
                    .bodyToMono(ApiResponse.class)
                    .block();
        }catch (WebClientResponseException e) {
            System.out.println("HTTP Status: " + e.getStatusCode());
            System.out.println("Response Body: " + e.getResponseBodyAsString());
            throw new SimoException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }catch (Exception e){
            throw new SimoException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

    }

    public ApiResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {

        SignedJWT signedJWT = SignedJWT.parse(request.getRefresh_token());
        String userName = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> new SimoException(ErrorCode.USER_NOT_FOUND));;
        String base = user.getConsumerKey() + ":" + user.getSecretKey();
        String base64Encode = Base64.getEncoder().encodeToString(base.getBytes());
        String url = "http://localhost:8085/api/v2/simo/refreshToken";
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(request.getRefresh_token(), request.getRefresh_token());
        try {
            return webClient.post()
                    .uri(url)
                    .header("Authorization", "Basic " + base64Encode)
                    .body(Mono.just(refreshTokenRequest), RefreshTokenRequest.class)
                    .retrieve()
                    .bodyToMono(ApiResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            System.out.println("HTTP Status: " + e.getStatusCode());
            System.out.println("Response Body: " + e.getResponseBodyAsString());
        }catch (Exception e){
            throw new SimoException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
            throw new SimoException(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }
}


