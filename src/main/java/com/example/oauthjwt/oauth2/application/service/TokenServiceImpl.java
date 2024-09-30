package com.example.oauthjwt.oauth2.application.service;

import com.example.oauthjwt.common.dto.TokenResponse;
import com.example.oauthjwt.common.exception.enums.TokenErrorResult;
import com.example.oauthjwt.common.exception.model.TokenException;
import com.example.oauthjwt.oauth2.domain.entity.RefreshToken;
import com.example.oauthjwt.oauth2.domain.repository.RefreshTokenRepository;
import com.example.oauthjwt.oauth2.infra.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME; // 액세스 토큰 유효기간

    private final RefreshTokenRepository refreshTokenRepository; // 도메인 리포지토리 사용
    private final JWTUtil jwtUtil;

    @Override
    public TokenResponse reissueAccessToken(String authorizationHeader) {
        String refreshToken = jwtUtil.getTokenFromHeader(authorizationHeader);
        String userId = jwtUtil.getUserIdFromToken(refreshToken);

        // 1. 도메인 객체를 사용하여 리프레시 토큰 조회
        RefreshToken existRefreshToken = refreshTokenRepository.findByUserId(UUID.fromString(userId))
                .orElseThrow(() -> new TokenException(TokenErrorResult.INVALID_REFRESH_TOKEN)); // 없을 경우 예외 처리

        // 2. 리프레시 토큰이 다르거나 만료된 경우 예외 처리
        if (!existRefreshToken.getToken().equals(refreshToken) || jwtUtil.isTokenExpired(refreshToken)) {
            throw new TokenException(TokenErrorResult.INVALID_REFRESH_TOKEN); // 401 에러를 던져 재로그인을 요청
        }

        // 3. 액세스 토큰 재발급
        String accessToken = jwtUtil.generateAccessToken(UUID.fromString(userId), ACCESS_TOKEN_EXPIRATION_TIME);

        // 4. TokenResponse 반환
        return TokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}