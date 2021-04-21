package com.lomari.redditclone.service;

import java.time.Instant;
import java.util.UUID;

import com.lomari.redditclone.exceptions.SpringRedditException;
import com.lomari.redditclone.models.RefreshToken;
import com.lomari.redditclone.repositories.RefreshTokenRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
// import lombok.NoArgsConstructor;


@Service
@AllArgsConstructor
// @NoArgsConstructor
public class RefreshTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken(){
        
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setCreatedDta(Instant.now());
        refreshToken.setToken(UUID.randomUUID().toString());


        return refreshTokenRepository.save(refreshToken);
        
    }

    public void validateRefreshToken(String refreshToken){
        refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new SpringRedditException("Invalid refresh token"));
    }

    public void deleteRefreshToken(String refreshToken){
        refreshTokenRepository.deleteByToken(refreshToken);
    }
}
