package com.example.cipherquest.persistence;

import com.example.cipherquest.model.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity,Long> {
}
