package com.example.oauthjwt.user.application.service;

import com.example.oauthjwt.user.application.dto.request.UserRegisterRequest;
import com.example.oauthjwt.user.application.dto.response.UserRegisterResponse;

public interface UserService {
    UserRegisterResponse registerUser(String registerToken, UserRegisterRequest request);
}
