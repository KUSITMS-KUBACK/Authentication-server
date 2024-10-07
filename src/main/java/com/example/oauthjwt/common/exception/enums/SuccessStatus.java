package com.example.oauthjwt.common.exception.enums;

import com.example.oauthjwt.common.exception.model.BaseCode;
import com.example.oauthjwt.common.dto.ReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    // 전역 응답 코드
    _OK(HttpStatus.OK, "200", "성공입니다."),
    _CREATED(HttpStatus.CREATED, "201", "생성에 성공했습니다."),

    // 커스텀 응답 코드
    _CREATED_ACCESS_TOKEN(HttpStatus.CREATED, "201", "액세스 토큰 재발행에 성공했습니다."),
    _REGISTER_USER(HttpStatus.CREATED, "201", "회원가입에 성공했습니다."),
    _OK_SEND_AUTH_CODE(HttpStatus.OK, "200", "휴대폰 인증 코드가 전송되었습니다."),
    _OK_VERIFY_AUTH_CODE(HttpStatus.OK, "200", "휴대폰 인증에 성공하였습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDto getReason() {
        return ReasonDto.builder()
                .isSuccess(true)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ReasonDto getReasonHttpStatus() {
        return ReasonDto.builder()
                .isSuccess(true)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}