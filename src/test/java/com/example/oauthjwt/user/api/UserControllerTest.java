package com.example.oauthjwt.user.api;

import com.example.oauthjwt.oauth2.infra.jwt.JWTUtil;
import com.example.oauthjwt.user.application.dto.request.SendAuthCodeReq;
import com.example.oauthjwt.user.application.dto.request.UserRegisterRequest;
import com.example.oauthjwt.user.application.dto.request.VerifyAuthCodeReq;
import com.example.oauthjwt.user.application.dto.response.UserRegisterResponse;
import com.example.oauthjwt.user.application.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JWTUtil jwtUtil;  // JWTUtil을 MockBean으로 등록

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"}) // 인증 추가
    public void registerUser_shouldReturnSuccess() throws Exception {
        // given
        UserRegisterRequest request = new UserRegisterRequest();
        request.setName("John Doe");
        request.setPhone("01012345678");

        UserRegisterResponse response = UserRegisterResponse.builder()
                .userId(UUID.randomUUID()) // UUID를 무작위로 생성
                .accessToken("dummyAccessToken")
                .refreshToken("dummyRefreshToken")
                .build();

        Mockito.when(userService.registerUser(Mockito.anyString(), Mockito.any(UserRegisterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/register")
                        .header("Authorization", "Bearer dummyRegisterToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())  // 상태 코드를 201로 맞추기
                .andExpect(jsonPath("$.payload.accessToken").value("dummyAccessToken"))  // JSON 경로 수정
                .andExpect(jsonPath("$.payload.refreshToken").value("dummyRefreshToken"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void sendAuthCode_shouldReturnSuccess() throws Exception {
        // given
        SendAuthCodeReq request = new SendAuthCodeReq("010-1234-5678");

        // when & then
        mockMvc.perform(post("/api/v1/send-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .with(csrf())) // CSRF 토큰 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("성공적으로 인증 코드를 보냈습니다."));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void verifyAuthCode_shouldReturnSuccess() throws Exception {
        // given
        VerifyAuthCodeReq request = new VerifyAuthCodeReq("010-1234-5678","123456");

        // when & then
        mockMvc.perform(post("/api/v1/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .with(csrf())) // CSRF 토큰 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("인증 코드가 확인되었습니다."));
    }
}