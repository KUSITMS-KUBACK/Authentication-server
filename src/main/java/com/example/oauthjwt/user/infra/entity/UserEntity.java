package com.example.oauthjwt.user.infra.entity;

import com.example.oauthjwt.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long id;

    @Column(name = "users_uuid", columnDefinition = "BINARY(16)", unique = true)
    private UUID userId;

    @Column(name = "name", nullable = false, length = 5)
    private String name;

    @Column(name = "provider", nullable = false, length = 10)
    private String provider;

    @Column(name = "provider_id", nullable = false, length = 50)
    private String providerId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, length = 20)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", length = 20)
    private LocalDateTime updatedAt;

    // 도메인 엔티티로 변환 메서드 (Optional)
    public User toDomain() {
        return User.builder()
                .userId(userId)
                .name(name)
                .provider(provider)
                .providerId(providerId)
                .build();
    }

    // 도메인 엔티티에서 infra 엔티티로 변환하는 메서드 (Optional)
    public static UserEntity fromDomain(User user) {
        return UserEntity.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .provider(user.getProvider())
                .providerId(user.getProviderId())
                .build();
    }
}