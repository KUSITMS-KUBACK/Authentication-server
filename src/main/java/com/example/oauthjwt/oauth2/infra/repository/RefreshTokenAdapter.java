package com.example.oauthjwt.oauth2.infra.repository;

import com.example.oauthjwt.oauth2.domain.entity.RefreshToken;
import com.example.oauthjwt.oauth2.domain.repository.RefreshTokenRepository;
import com.example.oauthjwt.oauth2.infra.entity.RefreshTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RefreshTokenAdapter implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;

    @Override
    public Optional<RefreshToken> findByUserId(UUID userId) {
        return jpaRefreshTokenRepository.findByUserId(userId)
                .map(RefreshTokenEntity::toDomain); // 엔티티 -> 도메인 변환
    }

    @Override
    public void deleteByUserId(UUID userId) {
        jpaRefreshTokenRepository.deleteByUserId(userId);
    }

    @Override
    public void save(RefreshToken refreshToken) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.fromDomain(refreshToken); // 도메인 -> 엔티티 변환
        jpaRefreshTokenRepository.save(refreshTokenEntity);
    }
}