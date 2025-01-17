package com.example.simo.service.simogw;

import com.example.simo.dto.request.CustomerAccountRequest;
import com.example.simo.dto.request.SuspectedFraudAccountRequest;
import com.example.simo.dto.request.UserAccountRequest;
import com.example.simo.dto.request.RefreshTokenRequest;
import com.example.simo.dto.response.ApiResponse;
import com.example.simo.exception.ErrorCode;
import com.example.simo.exception.SimoException;
import com.example.simo.model.User;
import com.example.simo.repository.UserRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


import java.text.ParseException;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SimoGWService {

    private final WebClient webClient;
    private final UserRepository userRepository;

    public ApiResponse sendToken(UserAccountRequest request) {
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(()-> new SimoException(ErrorCode.USER_NOT_FOUND));;
        String base = user.getConsumerKey() + ":" + user.getSecretKey();
        String base64Encode = Base64.getEncoder().encodeToString(base.getBytes());

        String apiUrl = "http://localhost:8085/api/v2/simo/getToken";
        UserAccountRequest requestPayload = new UserAccountRequest(request.getGrant_type(), request.getUserName(),
                request.getPassword());

        try{
            return webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Basic " + base64Encode)
                    .body(Mono.just(requestPayload), UserAccountRequest.class)
                    .retrieve()
                    .bodyToMono(ApiResponse.class)
                    .block();
        }catch (WebClientResponseException e) {
            System.out.println("HTTP Status: " + e.getStatusCode());
            System.out.println("Response Body: " + e.getResponseBodyAsString());
            JsonObject jsonObject = JsonParser.parseString(e.getResponseBodyAsString()).getAsJsonObject();
            String mess = jsonObject.get("message").getAsString();
            for(ErrorCode errorCode: ErrorCode.values()){
                if(errorCode.getMessage().equals(mess)){
                    throw new SimoException(errorCode);
                }
            }
            throw new SimoException(e.getMessage(), e);
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
            JsonObject jsonObject = JsonParser.parseString(e.getResponseBodyAsString()).getAsJsonObject();
            String mess = jsonObject.get("message").getAsString();
            for(ErrorCode errorCode: ErrorCode.values()){
                if(errorCode.getMessage().equals(mess)){
                    throw new SimoException(errorCode);
                }
            }
            throw new SimoException(ErrorCode.UNCATEGORIZED_EXCEPTION);

        }catch (SimoException e){
            throw new SimoException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ApiResponse getCustomerAccountList(String maYeuCau, String kyBaoCao, Set<CustomerAccountRequest> request) {

        String url = "http://localhost:8085/api/v2/simo/getListCustomerAccount";

        try {
            return webClient.post()
                    .uri(url)
                    .header("maYeuCau", maYeuCau)
                    .header("kyBaoCao", kyBaoCao)
                    .body(Mono.just(request), CustomerAccountRequest.class)
                    .retrieve()
                    .bodyToMono(ApiResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            System.out.println("HTTP Status: " + e.getStatusCode());
            System.out.println("Response Body: " + e.getResponseBodyAsString());

            //Xử lý trả về exception customer
            JsonObject jsonObject = JsonParser.parseString(e.getResponseBodyAsString()).getAsJsonObject();
            String mess = jsonObject.get("message").getAsString();
            for(ErrorCode errorCode: ErrorCode.values()){
                if(errorCode.getMessage().equals(mess)){
                    throw new SimoException(errorCode);
                }
            }
            throw new SimoException(ErrorCode.UNCATEGORIZED_EXCEPTION);

        } catch (SimoException e) {
            throw new SimoException(ErrorCode.UNCATEGORIZED_EXCEPTION);

        }
    }
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ApiResponse getSuspectFraudAccountList(String maYeuCau, String kyBaoCao, Set<SuspectedFraudAccountRequest> request) {

        String url = "http://localhost:8085/api/v2/simo/getListFraudAccount";

        try {
            return webClient.post()
                    .uri(url)
                    .header("maYeuCau", maYeuCau)
                    .header("kyBaoCao", kyBaoCao)
                    .body(Mono.just(request), SuspectedFraudAccountRequest.class)
                    .retrieve()
                    .bodyToMono(ApiResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            System.out.println("HTTP Status: " + e.getStatusCode());
            System.out.println("Response Body: " + e.getResponseBodyAsString());

            //Xử lý trả về exception customer
            JsonObject jsonObject = JsonParser.parseString(e.getResponseBodyAsString()).getAsJsonObject();
            String mess = jsonObject.get("message").getAsString();
            for(ErrorCode errorCode: ErrorCode.values()){
                if(errorCode.getMessage().equals(mess)){
                    throw new SimoException(errorCode);
                }
            }
            throw new SimoException(ErrorCode.UNCATEGORIZED_EXCEPTION);

        } catch (SimoException e) {
            throw new SimoException(ErrorCode.UNCATEGORIZED_EXCEPTION);

        }
    }
}


