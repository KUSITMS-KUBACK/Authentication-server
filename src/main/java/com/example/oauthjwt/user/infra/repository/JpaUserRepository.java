package com.example.oauthjwt.user.infra.repository;

import com.example.oauthjwt.user.infra.entity.UserEntity;
import com.example.oauthjwt.user.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u WHERE u.userId = :userId")
    Optional<UserEntity> findByUserId(UUID userId);

    Optional<UserEntity> findByProvider(String provider);
}