package com.example.oauthjwt;

import com.example.oauthjwt.common.dto.TokenResponse;
import com.example.oauthjwt.oauth2.application.service.TokenServiceImpl;
import com.example.oauthjwt.oauth2.domain.entity.RefreshToken;
import com.example.oauthjwt.oauth2.domain.repository.RefreshTokenRepository;
import com.example.oauthjwt.oauth2.infra.jwt.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private TokenServiceImpl tokenService;

    private String validRefreshToken;
    private String userId;
    private RefreshToken refreshToken;

    @BeforeEach
    void setup() {
        validRefreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOiI0ZjY1YjI4OC0wMTVhLTQ5MmMtYmQyYS05MmRiMWNiMmIwYTAiLCJpYXQiOjE3Mjc2Nzg2MDMsImV4cCI6MTcyODI4MzQwM30.pTAJIltt8cIeYN4hZ8L3lVS9houu9p6WOgD3WGh84j0cKc6GFNn8l_KV1f8D22isEQ3sMwnvHN5jgiWFnKyBuA";
        userId = UUID.randomUUID().toString();
        refreshToken = RefreshToken.builder()
                .userId(UUID.fromString(userId))
                .token(validRefreshToken)
                .build();
    }

    @Test
    void testReissueAccessToken_Success() {
        // Given
        when(jwtUtil.getTokenFromHeader(anyString())).thenReturn(validRefreshToken);
        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(userId);
        when(refreshTokenRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(refreshToken));
        when(jwtUtil.isTokenExpired(anyString())).thenReturn(false);
        when(jwtUtil.generateAccessToken(any(UUID.class), anyLong())).thenReturn("new-access-token");

        // When
        TokenResponse response = tokenService.reissueAccessToken("Bearer " + validRefreshToken);

        // Then
        assertEquals("new-access-token", response.getAccessToken());
        verify(refreshTokenRepository, times(1)).findByUserId(UUID.fromString(userId));
    }
}
