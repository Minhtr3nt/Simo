package com.example.simo.service.simo;

import com.example.simo.dto.response.TokenResponse;
import com.example.simo.model.Key;
import com.example.simo.model.Token;
import com.example.simo.model.User;
import com.example.simo.repository.KeyRepository;

import com.example.simo.repository.TokenRepository;
import com.example.simo.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SimoService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final KeyRepository keyRepository;

    @Value("${auth.token.expirationInMils}")
    protected Long VALID_TIME;

    @Value("${auth.token.expirationInMils2}")
    protected Long REFRESHABLE_TIME;

    @Value("${auth.token.jwtSecret}")
    protected String SIGNER_KEY;

    public TokenResponse getToken( String userName, String password ,String consumerKey, String secretKey){
        Key key = verifiedKey(consumerKey, secretKey);
        User user = verifiedUser(userName, password);
        if(key !=null&& user!=null) {
            return generateToken(user);

        }
        return generateToken(user);
    }

    public boolean verifiedToken(String token, boolean refresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = (refresh)?new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_TIME, ChronoUnit.SECONDS).toEpochMilli()) : signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);
        if(verified && expiryTime.after(new Date())){
            return true;
        }
        return false;
    }

    public TokenResponse refreshToken(String token) throws ParseException, JOSEException {

        if(verifiedToken(token, true)) {
            SignedJWT signer = SignedJWT.parse(token);

                String userName = signer.getJWTClaimsSet().getSubject();
                Key key = keyRepository.findByConsumerKey(userName);
                //return generateToken(user);
        }
        return null;
    }
    private Key verifiedKey(String consumerKey, String secretKey){
        Key key =  keyRepository.findByConsumerKey(consumerKey);
        if(key !=null) {
            boolean passValid = passwordEncoder.matches(secretKey, key.getSecretKey());

            if (passValid) {
                return key;
            }
        }
        return null;
    }
    private User verifiedUser(String userName, String password){
        User user =  userRepository.findByUserName(userName);
        if(user !=null) {
            boolean passValid = passwordEncoder.matches(password, user.getPassword());

            if (passValid) {
                return user;
            }
        }
        return null;
    }
    private String buildScope(User user){
        StringJoiner roles = new StringJoiner(" ");
        if(!user.getRoles().isEmpty()){
            user.getRoles().forEach(role -> {
                roles.add("ROLE_"+role.getName());
            });
            return roles.toString();
        }
        return null;

    }
    private TokenResponse generateToken(User user){

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_TIME, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {

            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setAccess_token(jwsObject.serialize());

            tokenResponse.setToken_type("Bearer");
            tokenResponse.setScope(buildScope(user));
            tokenResponse.setExpires_in(new Date(Instant.now().plus(VALID_TIME, ChronoUnit.SECONDS).toEpochMilli()));

            Token token = new Token();
            token.setToken(jwsObject.serialize());
            tokenRepository.save(token);

            return tokenResponse;
        } catch (JOSEException e) {

            throw new RuntimeException(e);
        }
    }
}
