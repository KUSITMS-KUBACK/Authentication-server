package com.example.oauthjwt.user.api;

import com.example.oauthjwt.common.dto.ApiResponse;
import com.example.oauthjwt.common.exception.enums.SuccessStatus;
import com.example.oauthjwt.user.application.dto.request.SendAuthCodeReq;
import com.example.oauthjwt.user.application.dto.request.UserRegisterRequest;
import com.example.oauthjwt.user.application.dto.request.VerifyAuthCodeReq;
import com.example.oauthjwt.user.application.dto.response.UserRegisterResponse;
import com.example.oauthjwt.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> registerUser(
            @RequestHeader("Authorization") String registerToken,
            @RequestBody UserRegisterRequest request) {

        UserRegisterResponse userRegisterResponse = userServiceImpl.registerUser(registerToken, request);
        return ApiResponse.onSuccess(SuccessStatus._REGISTER_USER, userRegisterResponse);
    }

    @PostMapping("/send-code")
    public ResponseEntity<ApiResponse<Void>> sendAuthCode(@RequestBody SendAuthCodeReq request) {
        userServiceImpl.sendAuthCode(request);
        return ApiResponse.onSuccess(SuccessStatus._OK_SEND_AUTH_CODE, null);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<ApiResponse<Void>> verifyAuthCode(@RequestBody VerifyAuthCodeReq request) {
        userServiceImpl.verifyAuthCode(request);
        return ApiResponse.onSuccess(SuccessStatus._OK_VERIFY_AUTH_CODE, null);
    }
}
