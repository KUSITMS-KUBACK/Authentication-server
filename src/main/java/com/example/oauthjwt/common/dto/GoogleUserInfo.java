package com.example.oauthjwt.common.dto;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class GoogleUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        // Google의 고유 ID는 sub 필드에 존재
        return attributes.get("sub").toString();
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getName() {
        // name 필드에서 사용자 이름 가져오기
        return attributes.get("name").toString();
    }
}