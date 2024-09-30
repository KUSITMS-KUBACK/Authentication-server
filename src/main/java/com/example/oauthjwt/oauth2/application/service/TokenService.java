package com.example.oauthjwt.oauth2.application.service;


import com.example.oauthjwt.common.dto.TokenResponse;

public interface TokenService {
    TokenResponse reissueAccessToken(String authorizationHeader);
}