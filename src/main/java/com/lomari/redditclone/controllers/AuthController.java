package com.lomari.redditclone.controllers;

import javax.validation.Valid;

import com.lomari.redditclone.DTOs.AuthenticationResponse;
import com.lomari.redditclone.DTOs.LoginRequest;
import com.lomari.redditclone.DTOs.RefreshTokenRequest;
import com.lomari.redditclone.DTOs.RegisterRequest;
import com.lomari.redditclone.service.AuthService;
import com.lomari.redditclone.service.RefreshTokenService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService auth;
    private final RefreshTokenService tokenService;
    
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest register){
        auth.signup(register);
        return new ResponseEntity<>("User registration was successful", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        auth.verifyAccount(token);
        return new ResponseEntity<String>("Acount activated", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return auth.login(loginRequest);
    }

    @PostMapping("refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshToken){
        return auth.refreshTokens(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshToken){
        tokenService.deleteRefreshToken(refreshToken.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK).body("Token deleted");
    }
}
