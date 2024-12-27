package com.example.cipherquest.service;

import com.example.cipherquest.persistence.StageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StageService {
    @Autowired
    private StageRepository stageRepository;
}
