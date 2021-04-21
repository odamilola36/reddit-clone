package com.lomari.redditclone.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.lomari.redditclone.DTOs.AuthenticationResponse;
import com.lomari.redditclone.DTOs.LoginRequest;
import com.lomari.redditclone.DTOs.RefreshTokenRequest;
import com.lomari.redditclone.DTOs.RegisterRequest;
import com.lomari.redditclone.exceptions.SpringRedditException;
import com.lomari.redditclone.models.NotificationEmail;
import com.lomari.redditclone.models.User;
import com.lomari.redditclone.models.VerificationToken;
import com.lomari.redditclone.repositories.UserRepository;
import com.lomari.redditclone.repositories.VerificationTokenRepository;
import com.lomari.redditclone.security.JwtProvider;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    
    @Transactional
    public void signup(RegisterRequest register){
        User user = new User();
        user.setEmail(register.getEmail());
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        user.setUsername(register.getUsername());
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateAuthenticationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your account", 
                        user.getEmail(), "please click on the url below to activate your account: "+
                        "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateAuthenticationToken(User user) {
        VerificationToken token = new VerificationToken();
        String verificationToken = UUID.randomUUID().toString();

        token.setUser(user);
        token.setToken(verificationToken); 

        tokenRepository.save(token);

        return verificationToken;
    }
    public void verifyAccount(String token){
        Optional<VerificationToken> verificationToken = tokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("invalid token"));

        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken){
        String username = verificationToken.getUser().getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> new SpringRedditException("user not found"));

        User user1 = user.get();
        user1.setEnabled(true);
        userRepository.save(user1);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtProvider.generateToken(auth);

        return AuthenticationResponse.builder()
                                    .authResponse(token)
                                    .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                                    .expiresAt(Instant.now().plusMillis(jwtProvider.getExpirationTime()))
                                    .username(loginRequest.getUsername())
                                    .build();
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).
                    orElseThrow(() -> new SpringRedditException(
                        String.format("user %s not found", username)));
    }

    public AuthenticationResponse refreshTokens(@Valid RefreshTokenRequest refreshToken) {
         refreshTokenService.validateRefreshToken(refreshToken.getRefreshToken());
         String token = 
         jwtProvider.generateTokenWithUsername(refreshToken.getUsername());

         return AuthenticationResponse.builder()
                                        .authResponse(token)
                                        .refreshToken(refreshToken.getRefreshToken())
                                        .expiresAt(Instant.now().plusMillis(jwtProvider.getExpirationTime()))
                                        .username(refreshToken.getUsername())
                                        .build();

    }
}
