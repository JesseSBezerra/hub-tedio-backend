package com.tedioinfernal.tedioapp.service;

import com.tedioinfernal.tedioapp.dto.AuthResponseDTO;
import com.tedioinfernal.tedioapp.dto.LoginRequestDTO;
import com.tedioinfernal.tedioapp.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponseDTO login(LoginRequestDTO loginRequest) {
        log.info("Attempting login for user: {}", loginRequest.getEmail());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            String token = jwtTokenProvider.generateToken(authentication);

            log.info("Login successful for user: {}", loginRequest.getEmail());
            
            return AuthResponseDTO.builder()
                    .token(token)
                    .build();
            
        } catch (AuthenticationException e) {
            log.error("Login failed for user: {}", loginRequest.getEmail());
            throw new RuntimeException("Email ou senha inválidos");
        }
    }
}
