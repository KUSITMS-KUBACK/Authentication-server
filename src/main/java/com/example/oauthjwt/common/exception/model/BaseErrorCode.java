package com.example.oauthjwt.common.exception.model;


import com.example.oauthjwt.common.dto.ErrorReasonDto;

public interface BaseErrorCode {
    public ErrorReasonDto getReason();

    public ErrorReasonDto getReasonHttpStatus();
}