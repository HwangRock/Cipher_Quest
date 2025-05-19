package com.example.cipherquest.persistence;

import com.example.cipherquest.model.Category;
import com.example.cipherquest.model.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity,Long> {
    Optional<PostEntity> findById(long id);
    List<PostEntity> findAllByCategoryAndIsdeletedFalse(Category category);
    Page<PostEntity> findAllByCategoryAndIsdeletedFalse(Category category, Pageable pageable);
}
