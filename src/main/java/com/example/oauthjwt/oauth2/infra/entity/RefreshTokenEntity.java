package com.example.oauthjwt.oauth2.infra.entity;

import com.example.oauthjwt.oauth2.domain.entity.RefreshToken;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(name = "refresh_tokens")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_tokens_id")
    private Long id;

    @Column(name = "users_uuid", columnDefinition = "BINARY(16)", unique = true)
    private UUID userId;

    @Column(name = "token", nullable = false)
    private String token;

    // 도메인 객체로 변환하는 메서드
    public RefreshToken toDomain() {
        return RefreshToken.builder()
                .userId(userId)
                .token(token)
                .build();
    }

    // 도메인 객체에서 엔티티로 변환하는 메서드
    public static RefreshTokenEntity fromDomain(RefreshToken refreshToken) {
        return RefreshTokenEntity.builder()
                .userId(refreshToken.getUserId())
                .token(refreshToken.getToken())
                .build();
    }
}