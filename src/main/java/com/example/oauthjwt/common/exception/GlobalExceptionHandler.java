package com.example.oauthjwt.common.exception;

import com.example.oauthjwt.common.exception.model.TokenException;
import com.example.oauthjwt.common.exception.model.UserException;
import com.example.oauthjwt.common.dto.ApiResponse;
import com.example.oauthjwt.common.exception.model.BaseErrorCode;
import com.example.oauthjwt.common.exception.enums.TokenErrorResult;
import com.example.oauthjwt.common.exception.enums.UserErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ApiResponse<BaseErrorCode>> handleTokenException(TokenException e) {
        TokenErrorResult errorResult = e.getTokenErrorResult();
        return ApiResponse.onFailure(errorResult);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse<BaseErrorCode>> handleUserException(UserException e) {
        UserErrorResult errorResult = e.getUserErrorResult();
        return ApiResponse.onFailure(errorResult);
    }
}