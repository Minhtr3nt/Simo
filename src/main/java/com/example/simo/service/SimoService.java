package com.example.simo.service;

import com.example.simo.model.User;
import com.example.simo.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("${auth.token.expirationInMils}")
    protected Long VALID_TIME;

    @Value("${auth.token.jwtSecret}")
    protected String SIGNER_KEY;

    public String getToken(String userName, String password){
        User user = verifiedUser(userName, password);

        if(user!=null) {
            return generateToken(user);
        }
        return null;
    }

    public String refreshToken(String token) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signer = SignedJWT.parse(token);

        if(signer.getJWTClaimsSet().getExpirationTime().toInstant().isBefore(Instant.now())) {


            String userName = signer.getJWTClaimsSet().getSubject();
            User user = userRepository.findByUserName(userName);
            return generateToken(user);

        }
        return null;
    }
    private User verifiedUser(String userName, String password){
        User user =  userRepository.findByUserName(userName);
        if(user!=null) {
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
    private String generateToken(User user){

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

            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
