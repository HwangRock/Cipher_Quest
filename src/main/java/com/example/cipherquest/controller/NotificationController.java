package com.example.cipherquest.controller;

import com.example.cipherquest.dto.ResponseDTO;
import com.example.cipherquest.model.NotificationEntity;
import com.example.cipherquest.service.JwtProvider;
import com.example.cipherquest.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping("/unread")
    public ResponseEntity<?> readNotification(@RequestHeader("Authorization") String authHeader){
        String userId=jwtProvider.extractUserId(authHeader);

        List<NotificationEntity> response=notificationService.readNotification(userId);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .isSuccess(true)
                        .responseDto(response)
                        .build()
        );
    }
}
