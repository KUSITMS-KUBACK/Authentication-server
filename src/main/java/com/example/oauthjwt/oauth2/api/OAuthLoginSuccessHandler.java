package com.example.oauthjwt.oauth2.api;

import com.example.oauthjwt.common.dto.KakaoUserInfo;
import com.example.oauthjwt.common.dto.OAuth2UserInfo;
import com.example.oauthjwt.oauth2.domain.entity.RefreshToken;
import com.example.oauthjwt.user.domain.entity.User;
import com.example.oauthjwt.oauth2.infra.jwt.JWTUtil;
import com.example.oauthjwt.oauth2.domain.repository.RefreshTokenRepository;
import com.example.oauthjwt.user.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${jwt.redirect.access}")
    private String ACCESS_TOKEN_REDIRECT_URI; // 기존 유저 로그인 시 리다이렉트 URI

    @Value("${jwt.redirect.register}")
    private String REGISTER_TOKEN_REDIRECT_URI; // 신규 유저 로그인 시 리다이렉트 URI

    @Value("${jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.refresh-token.expiration-time}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

    @Value(("600000"))
    private long REGISTER_TOKEN_EXPIRATION_TIME;

    private OAuth2UserInfo oAuth2UserInfo = null;

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository; // 도메인 UserRepository 사용
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        final String provider = token.getAuthorizedClientRegistrationId();

        switch (provider) {
            case "kakao" -> {
                log.info("카카오 로그인 요청");
                oAuth2UserInfo = new KakaoUserInfo(token.getPrincipal().getAttributes());
            }
        }

        // 정보 추출
        String providerId = oAuth2UserInfo.getProviderId();
        String name = oAuth2UserInfo.getName();

        // 1. Optional<User>로 처리
        Optional<User> optionalUser = userRepository.findByProviderId(providerId);
        User user;

        if (optionalUser.isEmpty()) {
            log.info("신규 유저입니다. 레지스터 토큰을 발급합니다.");
            String registerToken = URLEncoder.encode(jwtUtil.generateRegisterToken(provider, providerId, REGISTER_TOKEN_EXPIRATION_TIME));
            String encodedName = URLEncoder.encode(name, "UTF-8");
            String redirectUri = String.format(REGISTER_TOKEN_REDIRECT_URI, encodedName, registerToken);
            getRedirectStrategy().sendRedirect(request, response, redirectUri);
        } else {
            log.info("기존 유저입니다. 액세스 토큰과 리프레쉬 토큰을 발급합니다.");
            user = optionalUser.get();
            refreshTokenRepository.deleteByUserId(user.getUserId());
            // 리프레쉬 토큰 발급 후 저장
            String refreshToken = jwtUtil.generateRefreshToken(user.getUserId(), REFRESH_TOKEN_EXPIRATION_TIME);

            // 도메인 객체로 리프레시 토큰 생성
            RefreshToken refreshTokenDomain = RefreshToken.builder()
                    .userId(user.getUserId())
                    .token(refreshToken)
                    .build();

            // 도메인 객체 저장 (어댑터가 처리)
            refreshTokenRepository.save(refreshTokenDomain);

            // 액세스 토큰 발급
            String accessToken = jwtUtil.generateAccessToken(user.getUserId(), ACCESS_TOKEN_EXPIRATION_TIME);

            // 리다이렉트 처리
            String encodedName = URLEncoder.encode(name, "UTF-8");
            String redirectUri = String.format(ACCESS_TOKEN_REDIRECT_URI, encodedName, accessToken, refreshToken);
            getRedirectStrategy().sendRedirect(request, response, redirectUri);
        }
    }
}

