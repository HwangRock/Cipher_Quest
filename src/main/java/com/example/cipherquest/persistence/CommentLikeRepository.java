package com.example.cipherquest.persistence;

import com.example.cipherquest.model.CommentEntity;
import com.example.cipherquest.model.CommentLikeEntity;
import com.example.cipherquest.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity,Long> {
    Optional<CommentLikeEntity> findByUserAndComment(UserEntity user, CommentEntity comment);
    boolean existsByUserAndComment(UserEntity user, CommentEntity comment);
    long countByComment(CommentEntity comment);
    void deleteByUserAndComment(UserEntity user, CommentEntity comment);
}
