package com.example.oauthjwt.oauth2.domain.repository;


import com.example.oauthjwt.oauth2.domain.entity.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);
    void save(RefreshToken refreshToken);
}