package com.example.cipherquest.service;

import com.example.cipherquest.utils.cipher.EncryptStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class EncryptService {
    private Map<String, EncryptStrategy> strategyMap;

    @Autowired
    public void EncryptService(Map<String, EncryptStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public String Encrypt(String id, String text, String key) {
        EncryptStrategy strategy = strategyMap.get(id);
        if (strategy == null) {
            log.error("strategy is null for id: {}", id);
            throw new IllegalArgumentException("암호화 전략을 찾을 수 없습니다: " + id);
        }
        return strategy.encrypt(text, key);
    }

    public String Decrypt(String id, String text, String key) {
        EncryptStrategy strategy = strategyMap.get(id);
        if (strategy == null) {
            log.error("strategy is null");
        }
        return strategy.decrypt(text, key);
    }

    public String CreateKey(String id){
        EncryptStrategy strategy = strategyMap.get(id);
        if (strategy == null) {
            log.error("strategy is null");
        }
        return strategy.createRandomKey();
    }
}
