package com.example.oauthjwt.common.exception.model;

import com.example.oauthjwt.common.exception.enums.UserErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserException extends RuntimeException {
    private final UserErrorResult userErrorResult;

    @Override
    public String getMessage() {
        return userErrorResult.getMessage();
    }
}