package com.example.simo.controller;

import com.example.simo.service.SimoService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/token")
public class SimoController {

    private final SimoService simoService;


    @PostMapping("/getToken")
    public ResponseEntity<String> decodeAuthorization(@RequestHeader("Authorization") String authorizationHeader) {

        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {

            String base64Credentials = authorizationHeader.substring("Basic ".length()).trim();
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);


            String[] credentials = decodedString.split(":", 2);

            if (credentials.length == 2) {
                String consumerKey = credentials[0];
                String consumerSecret = credentials[1];
                String token = simoService.getToken(consumerKey, consumerSecret);
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid format");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid Authorization header!");
        }
    }





    @PostMapping("/refreshToken")
    public ResponseEntity<Object> refreshToken(@RequestBody String refreshToken) throws ParseException, JOSEException {
        String token = simoService.refreshToken(refreshToken);
        return ResponseEntity.ok().body(token);
    }
}
