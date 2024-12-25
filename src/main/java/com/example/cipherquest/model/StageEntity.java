package com.example.cipherquest.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Stage")
public class StageEntity {
    @Id
    private String stageId;
    private String description;
    private String encryptedText;
    private String answer;
}
