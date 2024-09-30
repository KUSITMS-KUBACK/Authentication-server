package com.example.oauthjwt.oauth2.infra.repository;

import com.example.oauthjwt.oauth2.infra.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaRefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    @Query("SELECT u FROM RefreshTokenEntity u WHERE u.userId = :userId")
    Optional<RefreshTokenEntity> findByUserId(UUID userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM RefreshTokenEntity u WHERE u.userId = :userId")
    void deleteByUserId(UUID userId);
}