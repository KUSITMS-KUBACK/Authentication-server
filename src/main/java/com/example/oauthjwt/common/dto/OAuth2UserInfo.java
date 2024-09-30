package com.example.oauthjwt.common.dto;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getName();
}