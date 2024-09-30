package com.example.oauthjwt.common.exception.model;


import com.example.oauthjwt.common.dto.ReasonDto;

public interface BaseCode {
    public ReasonDto getReason();

    public ReasonDto getReasonHttpStatus();
}