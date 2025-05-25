package com.example.cipherquest.service;

import com.example.cipherquest.model.NotificationEntity;
import com.example.cipherquest.model.UserEntity;
import com.example.cipherquest.persistence.NotificationRepository;
import com.example.cipherquest.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    public List<NotificationEntity> readNotification(String userId){
        Optional<UserEntity> requestUser=userRepository.findByUserid(userId);
        if(requestUser.isEmpty()){
            throw new RuntimeException("토큰에 하자 있음");
        }
        UserEntity user=requestUser.get();

        List<NotificationEntity>notificationEntities=notificationRepository.findByReceiverAndIsReadFalseOrderByCreatedAtDesc(user);

        return notificationEntities;
    }
}
