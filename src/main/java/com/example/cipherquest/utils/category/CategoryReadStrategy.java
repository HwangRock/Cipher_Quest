package com.example.cipherquest.utils.category;

import com.example.cipherquest.model.Category;
import com.example.cipherquest.model.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryReadStrategy {
    Category getCategory();
    List<PostEntity> readPosts();
    Page<PostEntity> readPostsPaged(Pageable pageable);
}
