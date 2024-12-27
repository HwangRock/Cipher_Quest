package com.example.cipherquest.controller;

import com.example.cipherquest.service.EncryptService;
import com.example.cipherquest.service.StageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/stage")
public class StageController {
    private StageService stageService;
    private EncryptService encryptService;
}
