package com.example.cipherquest.service;

import com.example.cipherquest.persistence.StageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StageService {
    @Autowired
    private StageRepository stageRepository;

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public StageService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //redis create, update
    public void setToRedis(String key, Object value) {
        try{
            redisTemplate.opsForValue().set(key, value);
        }catch(Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("Failed to set value to redis");
        }
    }

    //redis read
    public Object getFromRedis(String key){
        try{
            return redisTemplate.opsForValue().get(key);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("Failed to retrieve value from redis");
        }
    }

    //redis delete
    public void deleteFromRedis(String key){
        try{
            redisTemplate.delete(key);
        }catch(Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("Failed to delete value from redis");
        }
    }
}
