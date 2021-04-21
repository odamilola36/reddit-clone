package com.lomari.redditclone.repositories;

import java.util.Optional;

import com.lomari.redditclone.models.RefreshToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);
    
}
