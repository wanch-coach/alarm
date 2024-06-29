package com.wanchcoach.alarm.domain.controller;

import com.wanchcoach.alarm.domain.service.AlarmService;
import com.wanchcoach.alarm.domain.service.dto.RequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final AlarmService alarmService;

    @PostMapping("/api/fcm")
    public ResponseEntity<?> pushMessage(@RequestBody RequestDto requestDTO) throws IOException {
        System.out.println(requestDTO.getTargetToken()+" "+requestDTO.getTitle() + " " + requestDTO.getBody());

        alarmService.sendMessageTo(
                requestDTO.getTargetToken(),
                requestDTO.getTitle(),
                requestDTO.getBody());

        return ResponseEntity.ok("zz");
    }
}
