package com.example.oauthjwt.user.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class UserRegisterResponse {
    private UUID userId;
    private String refreshToken;
    private String accessToken;
}
