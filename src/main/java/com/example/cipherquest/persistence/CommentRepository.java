package com.example.cipherquest.persistence;

import com.example.cipherquest.model.CommentEntity;
import com.example.cipherquest.model.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    Optional<CommentEntity> findById(long id);
    List<CommentEntity> findAllByPostIdAndIsDeletedFalse(PostEntity post);
}
