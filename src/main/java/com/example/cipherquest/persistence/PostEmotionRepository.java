package com.example.cipherquest.persistence;

import com.example.cipherquest.model.EmotionType;
import com.example.cipherquest.model.PostEmotionEntity;
import com.example.cipherquest.model.PostEntity;
import com.example.cipherquest.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostEmotionRepository extends JpaRepository<PostEmotionEntity, Long> {
    Optional<PostEmotionEntity> findByUserAndPost(UserEntity user, PostEntity post);
    long countByPostAndEmotionType(Optional<PostEntity> post, EmotionType emotionType);
    boolean existsByUserAndPost(UserEntity user, PostEntity post);
}
