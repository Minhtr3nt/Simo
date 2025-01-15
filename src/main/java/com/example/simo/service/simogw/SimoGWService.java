package com.example.simo.service.simogw;

import com.example.simo.dto.request.AccountRequest;
import com.example.simo.dto.request.RefreshTokenRequest;
import com.example.simo.dto.response.ApiResponse;
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
public class SimoGWService  {

    private final WebClient webClient;
    private final UserRepository userRepository;

    public ApiResponse sendToken (AccountRequest request) {
        User user = userRepository.findByUserName(request.getUserName());
        String base = user.getConsumerKey()+":"+user.getSecretKey();
        String base64Encode = Base64.getEncoder().encodeToString(base.getBytes());

        String apiUrl = "http://localhost:8085/api/v2/simo/getToken";
        AccountRequest requestPayload = new AccountRequest(request.getGrant_type(),request.getUserName(),
                                                            request.getPassword());

            return webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Basic " + base64Encode)
                    .body(Mono.just(requestPayload), AccountRequest.class)
                    .retrieve()
                    .bodyToMono(ApiResponse.class)
                    .block();

    }

    public ApiResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {

        SignedJWT signedJWT = SignedJWT.parse(request.getRefresh_token());
        String userName = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUserName(userName);
        String base = user.getConsumerKey() + ":" + user.getSecretKey();
        String base64Encode = Base64.getEncoder().encodeToString(base.getBytes());
        String url= "http://localhost:8085/api/v2/simo/refreshToken";
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(request.getRefresh_token(), request.getRefresh_token());

        return webClient.post()
                .uri(url)
                .header("Authorization","Basic "+base64Encode)
                .body(Mono.just(refreshTokenRequest), RefreshTokenRequest.class)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .block();
    }
}
