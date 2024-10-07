package com.example.oauthjwt.user.application.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.from_number}")
    private String fromNumber;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }

    public void sendSms(String to, String message) {
        // 하이픈 제거 및 E.164 형식으로 변환 // 숫자만 남기고 앞의 0 제거
        String formattedPhoneNumber = "+82" + to.replaceAll("[^0-9]", "").substring(1);
        Message.creator(
                new PhoneNumber(formattedPhoneNumber), // 수신자 번호 (E.164 형식)
                new PhoneNumber(fromNumber),          // 발신자 번호 (Twilio 제공 번호)
                message                               // 메시지 내용
        ).create();
    }
}