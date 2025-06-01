package com.example.cipherquest.persistence;

import com.example.cipherquest.model.NotificationEntity;
import com.example.cipherquest.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByReceiverAndIsReadFalseOrderByCreatedAtDesc(UserEntity receiver);
    Optional<NotificationEntity> findById(long id);
}
