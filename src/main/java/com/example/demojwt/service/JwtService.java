package com.example.demojwt.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
public class JwtService {
    public static final String USERNAME = "username";
    public static final String SECRET_KEY = "11111111111111111111111111111111";
    public static final int EXPIRE_TIME = 86400000;

    public String generateTokenLogin(String username){
        String token = null;
        try{
            JWSSigner signer = new MACSigner(generateShareSecret());

            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
            builder.claim(USERNAME,username);
            builder.expirationTime(generateExpirationDate());

            JWTClaimsSet claimsSet = builder.build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

            signedJWT.sign(signer);

            token = signedJWT.serialize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return token;
    }

    private JWTClaimsSet getClaimsFromToken(String token){
        JWTClaimsSet claims = null;
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(generateShareSecret());
            if(signedJWT.verify(verifier)){
                claims = signedJWT.getJWTClaimsSet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return claims;
    }

    public byte[] generateShareSecret(){
        byte[] sharedSecret = new byte[32];
        sharedSecret = SECRET_KEY.getBytes();
        return sharedSecret;
    }

    private Date generateExpirationDate(){
        return new Date(System.currentTimeMillis() + EXPIRE_TIME);
    }

    private Date getExpirationDateFromToken(String token){
        Date expiration = null;
        JWTClaimsSet claims = getClaimsFromToken(token);
        expiration = claims.getExpirationTime();
        return expiration;
    }

    public String getUsernameFromToken(String token){
        String username = null;
        JWTClaimsSet claims = getClaimsFromToken(token);
        try {
            username = claims.getStringClaim(USERNAME);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return username;
    }

    private Boolean isTokenExpired(String token){
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateTokenLogin(String token){
        if(token == null || token.trim().length() == 0){
            return false;
        }
        String username = getUsernameFromToken(token);

        if(username == null || username.isEmpty()){
            return false;
        }

        if(isTokenExpired(token)){
            return false;
        }

        return true;
    }
}

