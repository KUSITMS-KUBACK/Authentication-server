package com.example.oauthjwt.user.application.dto.request;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String name;
    private String phone;
}
