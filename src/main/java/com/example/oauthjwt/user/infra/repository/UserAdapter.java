package com.example.oauthjwt.user.infra.repository;

import com.example.oauthjwt.user.domain.entity.User;
import com.example.oauthjwt.user.domain.repository.UserRepository;
import com.example.oauthjwt.user.infra.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public Optional<User> findByUserId(UUID userId) {
        return jpaUserRepository.findByUserId(userId)
                .map(UserEntity::toDomain); // UserEntity -> User 변환
    }

    @Override
    public Optional<User> findByProvider(String provider) {
        // Optional을 사용하여 null 체크 처리
        return jpaUserRepository.findByProvider(provider)
                .map(UserEntity::toDomain);
    }

    @Override
    public void save(User user) {
        UserEntity userEntity = UserEntity.fromDomain(user); // User -> UserEntity 변환
        jpaUserRepository.save(userEntity);
    }

    @Override
    public User findByPhone(String phone) {
        return null;
    }
}