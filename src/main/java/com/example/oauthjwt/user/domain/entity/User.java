package com.example.oauthjwt.user.domain.entity;

import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User {
    private UUID userId;
    private String name;
    private String provider;
    private String providerId;
    private String phone;
}