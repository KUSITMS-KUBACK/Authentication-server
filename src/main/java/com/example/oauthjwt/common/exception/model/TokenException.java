package com.example.oauthjwt.common.exception.model;

import com.example.oauthjwt.common.exception.enums.TokenErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenException extends RuntimeException {
    private final TokenErrorResult tokenErrorResult;

    @Override
    public String getMessage() {
        return tokenErrorResult.getMessage();
    }
}