package com.example.send.otp.with.twillio.controller;

import com.example.send.otp.with.twillio.dto.SmsPojo;
import com.example.send.otp.with.twillio.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class SmsController {

    @Autowired
    SmsService service;

    @Autowired
    private SimpMessagingTemplate websocket;

    private final String TOPIC_DESTINATION = "/lesson/sms";

    @PostMapping("/mobileNo")
    public ResponseEntity<Boolean> smsSubmit(@RequestBody SmsPojo smsPojo) {
        try {
            service.send(smsPojo);
        } catch (Exception e) {
            return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        websocket.convertAndSend(TOPIC_DESTINATION, getTimeStamp() + ": SMS has been sent");
        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @RequestMapping(value = "/smscallback", method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void smsCallback(@RequestBody MultiValueMap<String, String> map) {
        service.receive(map);
        websocket.convertAndSend(TOPIC_DESTINATION, getTimeStamp() + "Twillio has made a callback request! Here are the content:" + map.toString());
    }

    private String getTimeStamp() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
    }
}
