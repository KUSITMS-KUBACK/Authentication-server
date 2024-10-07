package com.example.oauthjwt.common.exception.enums;

import com.example.oauthjwt.common.dto.ErrorReasonDto;
import com.example.oauthjwt.common.exception.model.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorResult implements BaseErrorCode {
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "404", "존재하지 않는 유저입니다."),
    _FAILED_SAVE_REDIS(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Redis 저장에 실패하였습니다."),
    _EXPIRED_AUTH_CODE(HttpStatus.UNAUTHORIZED, "8104", "인증코드가 만료되었습니다. 인증코드를 재발급해주세요."),
    _MISS_MATCH_AUTH_CODE(HttpStatus.BAD_REQUEST, "8105", "인증코드가 일치하지 않습니다."),
    _EXISTING_USER_ACCOUNT_KAKAO(HttpStatus.FORBIDDEN, "8106", "이미 회원가입된 카카오 계정이 존재합니다."),
    _EXISTING_USER_ACCOUNT_GOOGLE(HttpStatus.FORBIDDEN, "8107", "이미 회원가입된 구글 계정이 존재합니다."),
    _EXISTING_USER_ACCOUNT_NAVER(HttpStatus.FORBIDDEN, "8108", "이미 회원가입된 네이버 계정이 존재합니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .isSuccess(false)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}