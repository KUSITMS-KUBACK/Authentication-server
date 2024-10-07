package com.example.oauthjwt.oauth2.infra.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("JWTFilter: 요청 처리 시작 " );

            // 쿠키에서 액세스 토큰 가져오기
            String accessToken = getAccessTokenFromCookies(request);
            if (accessToken != null) {
                // 토큰 검증
                jwtUtil.validateToken(accessToken);

                // 유효한 경우 사용자 인증 설정
                String userId = jwtUtil.getUserIdFromToken(accessToken);
                setAuthentication(request, userId);
            }
        } catch (Exception e) {
            // 토큰이 유효하지 않거나 인증이 실패한 경우 예외 처리
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 Unauthorized 응답
            return;  // 필터 체인을 종료
        }
        filterChain.doFilter(request, response);
    }

    // 쿠키에서 액세스 토큰 추출
    private String getAccessTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void setAuthentication(HttpServletRequest request, String userId) {
        // Spring Security가 제공하는 UsernamePasswordAuthenticationToken을 사용
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, null);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // SecurityContextHolder에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
