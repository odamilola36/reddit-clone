package com.lomari.redditclone.repositories;


import java.util.Optional;

import com.lomari.redditclone.models.VerificationToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
