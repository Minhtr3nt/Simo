package com.example.simo.service.simogw;

import com.example.simo.dto.request.AccountRequest;
import com.example.simo.dto.response.ApiResponse;
import com.example.simo.model.Key;
import com.example.simo.repository.KeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.Base64;

@Service
@RequiredArgsConstructor
public class SimoGWService {

    private final WebClient webClient;
    private final KeyRepository keyRepository;

    public ApiResponse sendToken (AccountRequest request){
        Key key= keyRepository.findByUser_UserName(request.getUserName());
        String base = key.getConsumerKey()+":"+key.getSecretKey();
        String base64Encode = Base64.getEncoder().encodeToString(base.getBytes());

        String apiUrl = "http://localhost:8085/api/v2/token/getToken";
        AccountRequest requestPayload = new AccountRequest("password", request.getUserName(), request.getPassword());

        return webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Basic " + base64Encode)
                .body(Mono.just(requestPayload), AccountRequest.class)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .block();


    }
}
