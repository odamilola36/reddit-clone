package com.lomari.redditclone.DTOs;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String authResponse;
    private String refreshToken;
    private Instant expiresAt;
    private String username;   
    
}
