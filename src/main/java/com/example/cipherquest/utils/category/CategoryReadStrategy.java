package com.example.cipherquest.utils.category;

import com.example.cipherquest.model.Category;
import com.example.cipherquest.model.PostEntity;

import java.util.List;

public interface CategoryReadStrategy {
    Category getCategory();
    List<PostEntity> readPosts();
}
