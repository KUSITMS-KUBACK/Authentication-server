package com.example.oauthjwt.user.application.service;

import com.example.oauthjwt.user.application.dto.request.SendAuthCodeReq;
import com.example.oauthjwt.user.application.dto.request.UserRegisterRequest;
import com.example.oauthjwt.user.application.dto.request.VerifyAuthCodeReq;
import com.example.oauthjwt.user.application.dto.response.UserRegisterResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {
    UserRegisterResponse registerUser(String registerToken, UserRegisterRequest request);
    void sendAuthCode(SendAuthCodeReq request);
    void verifyAuthCode(VerifyAuthCodeReq request);
}
