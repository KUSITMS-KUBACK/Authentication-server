package com.example.oauthjwt.oauth2.api;

import com.example.oauthjwt.common.dto.ApiResponse;
import com.example.oauthjwt.common.exception.enums.SuccessStatus;
import com.example.oauthjwt.common.dto.TokenResponse;
import com.example.oauthjwt.oauth2.application.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TokenController {
    @Value("${jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;
    private final TokenService authService;

    // 액세스 토큰을 재발행하는 API (쿠키 방식)
    @GetMapping("/reissue/access-token")
    public ResponseEntity<ApiResponse<Object>> reissueAccessToken(
            @CookieValue(value = "refreshToken", required = false) String refreshTokenCookie, // 쿠키에서 리프레시 토큰 가져옴
            HttpServletResponse response) {

        // 서비스에서 액세스 토큰 재발급 처리
        TokenResponse accessToken = authService.reissueAccessToken(refreshTokenCookie);

        // 새 액세스 토큰을 쿠키로 추가
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken.getAccessToken());
        accessTokenCookie.setHttpOnly(true); // 보안 설정
        accessTokenCookie.setSecure(true);   // HTTPS에서만 전송
        accessTokenCookie.setPath("/");      // 모든 경로에서 접근 가능
        accessTokenCookie.setMaxAge((int) ACCESS_TOKEN_EXPIRATION_TIME / 1000); // 만료 시간 설정
        response.addCookie(accessTokenCookie);

        return ApiResponse.onSuccess(SuccessStatus._CREATED_ACCESS_TOKEN, null);
    }
}