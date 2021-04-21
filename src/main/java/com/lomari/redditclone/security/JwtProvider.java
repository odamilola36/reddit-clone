package com.lomari.redditclone.security;


import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

import javax.annotation.PostConstruct;

import com.lomari.redditclone.exceptions.SpringRedditException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtProvider {
    
    private KeyStore keystore;
    
    @Value("${jwt.expiration.time}")
    private Long tokenExpirationDate;

    @PostConstruct
    public void init() {
        try {
            keystore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keystore.load(resourceAsStream, "secret".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new SpringRedditException("Exception occurred while loading keystore");
        } 
    }

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        return Jwts.builder()
                    .setSubject(username)
                    .setExpiration(Date.from(Instant.now().plusMillis(tokenExpirationDate)))
                    .signWith(getPrivateke())
                    .compact();
    }

    private Key getPrivateke() {
        try {
            return (PrivateKey) keystore.getKey("springblog", "secret".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException |UnrecoverableKeyException e) {
            throw new SpringRedditException("Exeption occured while retrieving public key from key store");
        }
    }

    public boolean validateToken(String jwt){
        Jwts.parserBuilder()
            .setSigningKey(getPublicKey())
            .build().parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublicKey() {
        try {
            return keystore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringRedditException("Exception occurred while retrieving public key from key store");
        }
    }

    public String getUsernameFromToken(String token){
        Claims claims = Jwts.parserBuilder()
                            .setSigningKey(getPublicKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
        return claims.getSubject();
    }

    public Long getExpirationTime(){
        return tokenExpirationDate;
    }

    public String generateTokenWithUsername(String username){
        return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(Date.from(Instant.now()))
                    .signWith(getPrivateke())
                    .setExpiration(Date.from(Instant.now().plusMillis(tokenExpirationDate)))
                    .compact();
    }
}
