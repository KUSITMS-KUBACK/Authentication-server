package com.example.oauthjwt.user.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendAuthCodeReq {
    private String phone;
}
