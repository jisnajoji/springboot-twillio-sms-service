package com.example.send.otp.with.twillio.service;

import com.example.send.otp.with.twillio.dto.SmsPojo;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.text.ParseException;

@Component
public class SmsService {
    private final String ACCOUNT_SID = "AC968bf72be78c7ab102747cd5e5f96dde";
    private final String AUTH_TOKEN = "a45006fe1429c82e5104a9a07fe70512";
    private final String FROM_NUMBER = "+19893822360";

    public void send(SmsPojo smsPojo) throws ParseException {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        int min = 100000;
        int max = 999999;
        int number = (int)(Math.random()*(max-min+1)+min);

        String msg = "Your OTP"+ number + "pls verify this OTP ";

        Message message = Message.creator(new PhoneNumber(smsPojo.getPhoneNo()), new PhoneNumber(FROM_NUMBER), msg)
                .create();
    }

    public void receive(MultiValueMap<String, String> map) {
    }
}
