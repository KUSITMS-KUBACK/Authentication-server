package com.example.oauthjwt.user.application.service;

public interface SmsService {

    void initTwilio();
    void sendSms(String to, String message);
}
