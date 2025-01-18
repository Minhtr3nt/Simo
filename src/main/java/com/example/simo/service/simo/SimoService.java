package com.example.simo.service.simo;

import com.example.simo.dto.request.CustomerAccountRequest;
import com.example.simo.dto.request.RefreshTokenRequest;
import com.example.simo.dto.request.SuspectedFraudAccountRequest;
import com.example.simo.dto.response.ApiResponse;
import com.example.simo.dto.response.TokenResponse;
import com.example.simo.exception.ErrorCode;
import com.example.simo.exception.SimoException;
import com.example.simo.mapper.CustomerAccountMapper;
import com.example.simo.model.*;

import com.example.simo.repository.collect.CustomerAccountRepository;
import com.example.simo.repository.collect.ReportRepository;
import com.example.simo.repository.TokenRepository;
import com.example.simo.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimoService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;
    private final CustomerAccountMapper customerAccountMapper;
    private final ReportRepository reportRepository;



    @Value("${auth.token.expirationInMils}")
    protected Long VALID_TIME;

    @Value("${auth.token.expirationInMils2}")
    protected Long REFRESHABLE_TIME;

    @Value("${auth.token.jwtSecret}")
    protected String SIGNER_KEY;

    public ApiResponse collectCustomerAccount(String maYeuCau, String kyBaoCao, Set<CustomerAccountRequest> request)  {
       // redisTemplate.opsForList().leftPushAll(key, requests);

        int batchSize = 100;
        List<List<CustomerAccountRequest>> cusAccountBatches = new ArrayList<>();
        List<CustomerAccountRequest> batch = new ArrayList<>();
        for(CustomerAccountRequest account: request){
            batch.add(account);
            if(batch.size()>=batchSize){
                cusAccountBatches.add(batch);
                batch.clear();
            }
        }
        if(cusAccountBatches.isEmpty()){
            cusAccountBatches.add(batch);
        }
        ExecutorService executorService =  Executors.newFixedThreadPool(cusAccountBatches.size());
       try {

           List<Future<?>> futures = new ArrayList<>();
           for(List<CustomerAccountRequest> batch2: cusAccountBatches ) {
                futures.add(executorService.submit(() -> saveCustomerAccount(maYeuCau, kyBaoCao, batch2)));

           }
           for(Future<?> future: futures) {
               future.get();
           }
           return new ApiResponse(0, "Save successful", null);

       }catch (ExecutionException | InterruptedException e){
            throw new SimoException(ErrorCode.STOP_SAVE_PROCESS);
       }finally {
           executorService.shutdown();
       }

    }

    private void saveCustomerAccount(String maYeuCau, String kyBaoCao, List<CustomerAccountRequest> request){
        ReportCustomerAccount report = new ReportCustomerAccount();
        report.setMaYeuCau(maYeuCau);
        report.setKyBaoCao(kyBaoCao);
        report.setLoaiBaoCao("Thu thập danh sách TKTT định kỳ");

        Set<CustomerAccount> customerAccounts = request.stream()
                .map(customerAccountMapper::toCustomerAccount)
                .peek(customerAccount -> customerAccount.setReportCustomerAccount(report))
                .collect(Collectors.toSet());
        report.setCustomerAccounts(customerAccounts);
        reportRepository.save(report);


    }

    public ApiResponse collectSuspectFraudAccount(String maYeuCau, String kyBaoCao, Set<SuspectedFraudAccountRequest> requests){
        int batch_size = 100;
        List<List<SuspectedFraudAccountRequest>> batchs = new ArrayList<>();
        List<SuspectedFraudAccountRequest> batch = new ArrayList<>();
        for(SuspectedFraudAccountRequest account : requests){
            batch.add(account);
            if(batch.size()>=100){
                batchs.add(batch);
                batch.clear();
            }
        }
        if(batchs.isEmpty()){
            batchs.add(batch);
            batch.clear();
        }
        ExecutorService executorService = Executors.newFixedThreadPool(batchs.size());

        try {
            List<Future<?>> futures = new ArrayList<>();
            for (List<SuspectedFraudAccountRequest> batch1 : batchs) {
                futures.add(executorService.submit(() -> saveFraudCustomerAccount(maYeuCau, kyBaoCao, batch1)));
            }
            for (Future<?> future : futures) {
                future.get();
            }

        }catch(ExecutionException| InterruptedException e){
            throw new SimoException(ErrorCode.STOP_SAVE_PROCESS);
        }
        executorService.shutdown();
        return new ApiResponse(0,"Successful", true);
    }

    private void saveFraudCustomerAccount(String maYeuCau, String kyBaoCao, List<SuspectedFraudAccountRequest> requests){

        ReportCustomerAccount report = new ReportCustomerAccount();
        report.setMaYeuCau(maYeuCau);
        report.setKyBaoCao(kyBaoCao);
        report.setLoaiBaoCao("Thu thập danh sách TKTT nghi ngờ gian lận");

        if(reportRepository.existsById(maYeuCau)){
            throw new SimoException(ErrorCode.REPORT_CODE_INVALID);
        }
        Set<SuspectedFraudAccount> accounts = requests.stream()
                .map(account-> modelMapper.map(account,SuspectedFraudAccount.class ))
                .peek(customerAccount -> customerAccount.setReportCustomerAccount(report))
                .collect(Collectors.toSet());

        report.setSuspectedFraudAccounts(accounts);

        reportRepository.save(report);

    }

    // Xử lý tạo, xác thực, refresh token.

    private final CustomerAccountRepository customerAccountRepository;

    public TokenResponse getToken( String userName, String password ,String consumerKey, String secretKey){

        User user = verifiedUser(userName, password, consumerKey, secretKey);
        deleteNotUseToken(user);
        return generateToken(user);
    }

    public TokenResponse refreshToken(String consumerKey, String secretKey, RefreshTokenRequest refreshTokenRequest)
            throws ParseException, JOSEException {

        User user = verifiedKey(consumerKey, secretKey);
        String token = refreshTokenRequest.getRefresh_token();

        if(verifiedToken(token, true)) {
            Token token2 = tokenRepository.findByToken(token)
                    .orElseThrow(()-> new SimoException(ErrorCode.TOKEN_NOT_FOUND));
            tokenRepository.delete(token2);

            return generateToken(user);
        }
        throw new SimoException(ErrorCode.TOKEN_EXPIRED_REFRESH);
    }

    public boolean verifiedToken(String token, boolean refresh) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = (refresh)?new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
                .plus(REFRESHABLE_TIME, ChronoUnit.SECONDS).toEpochMilli())
                    : signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);

        if(verified && expiryTime.after(new Date())){
            return true;
        }

        throw new SimoException(ErrorCode.TOKEN_EXPIRED);
    }
    public void deleteNotUseToken(User user){

       List<Token> tokens =  tokenRepository.findByUser_UserName(user.getUserName());
       if(tokens.isEmpty()){
           return;
       }
       tokens.forEach(tokenRepository::delete);
    }


    private User verifiedKey(String consumerKey, String secretKey){
        User user = userRepository.findByConsumerKey(consumerKey)
                .orElseThrow(()-> new SimoException(ErrorCode.USER_NOT_FOUND));;
        if(secretKey.equals(user.getSecretKey())) {
                return user;
        }
        throw new SimoException(ErrorCode.KEY_INVALID);
    }
    private User verifiedUser(String userName, String password, String consumerKey, String secretKey){
        User user =  userRepository.findByUserName(userName)
                .orElseThrow(()-> new SimoException(ErrorCode.USER_NOT_FOUND));;

        if(user !=null) {
            boolean passValid = passwordEncoder.matches(password, user.getPassword());

            if (passValid && user.getSecretKey().equals(secretKey)) {
                return user;
            }else{
                throw new SimoException(ErrorCode.PASS_INVALID);
            }
        }
        throw new SimoException(ErrorCode.USER_INVALID);

    }

    private TokenResponse generateToken(User user){

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(VALID_TIME, ChronoUnit.SECONDS)
                        .toEpochMilli()))
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
            tokenResponse.setExpires_in(new Date(Instant.now()
                    .plus(VALID_TIME, ChronoUnit.SECONDS)
                    .toEpochMilli()));

            Token token = new Token();
            token.setToken(jwsObject.serialize());
            token.setUser(user);
            tokenRepository.save(token);

            return tokenResponse;

        } catch (JOSEException e) {

            throw new RuntimeException(e);
        }
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
}
