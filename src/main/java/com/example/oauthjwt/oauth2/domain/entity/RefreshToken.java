package com.example.oauthjwt.oauth2.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class RefreshToken {
    private UUID userId;
    private String token;
}