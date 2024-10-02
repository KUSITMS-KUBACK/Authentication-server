package com.example.oauthjwt.user.application.service;

import com.example.oauthjwt.common.exception.enums.TokenErrorResult;
import com.example.oauthjwt.common.exception.model.TokenException;
import com.example.oauthjwt.oauth2.domain.entity.RefreshToken;
import com.example.oauthjwt.oauth2.domain.repository.RefreshTokenRepository;
import com.example.oauthjwt.oauth2.infra.jwt.JWTUtil;
import com.example.oauthjwt.user.application.dto.request.UserRegisterRequest;
import com.example.oauthjwt.user.application.dto.response.UserRegisterResponse;
import com.example.oauthjwt.user.domain.entity.User;
import com.example.oauthjwt.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.refresh-token.expiration-time}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

    @Override
    public UserRegisterResponse registerUser(String registerToken, UserRegisterRequest request) {
        if (jwtUtil.isTokenExpired(registerToken)) {
            throw new TokenException(TokenErrorResult.INVALID_REFRESH_TOKEN);
        }

        String providerId = jwtUtil.getProviderIdFromToken(registerToken);
        String provider = jwtUtil.getProviderFromToken(registerToken);

        // 휴대폰 인증 추가 필요

        User newUser = saveUser(request, providerId, provider);
        String refreshToken = generateRefreshToken(newUser);
        String accessToken = jwtUtil.generateAccessToken(newUser.getUserId(), ACCESS_TOKEN_EXPIRATION_TIME);

        return UserRegisterResponse.builder()
                .userId(newUser.getUserId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String generateRefreshToken(User newUser) {
        String refreshToken = jwtUtil.generateRefreshToken(newUser.getUserId(), REFRESH_TOKEN_EXPIRATION_TIME);
        RefreshToken refreshTokenDomain = RefreshToken.builder()
                .userId(newUser.getUserId())
                .token(refreshToken)
                .build();
        refreshTokenRepository.save(refreshTokenDomain);
        return refreshToken;
    }

    private User saveUser(UserRegisterRequest request, String providerId, String provider) {
        User newUser = User.builder()
                .userId(UUID.randomUUID())
                .providerId(providerId)
                .name(request.getName())
                .phone(request.getPhone())
                .provider(provider)
                .build();
        userRepository.save(newUser);
        return newUser;
    }
}
