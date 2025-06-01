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

    public NotificationEntity patchReadNotification(String userId, long id){

        Optional<NotificationEntity> requestId=notificationRepository.findById(id);
        if(requestId.isEmpty()){
            throw new RuntimeException("존재 ㄴㄴ 알람");
        }
        NotificationEntity notification=requestId.get();

        if (!notification.getReceiver().getUserid().equals(userId)) {
            throw new RuntimeException("본인의 알림만 읽을 수 있음.");
        }

        notification.setRead(true);

        return notificationRepository.save(notification);
    }
}
