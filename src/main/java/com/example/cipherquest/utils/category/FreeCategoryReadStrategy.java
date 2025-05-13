package com.example.cipherquest.utils.category;

import com.example.cipherquest.model.Category;
import com.example.cipherquest.model.PostEntity;
import com.example.cipherquest.persistence.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("free")
public class FreeCategoryReadStrategy implements CategoryReadStrategy {
    @Autowired
    private PostRepository postRepository;

    @Override
    public Category getCategory() {
        return Category.FREE;
    }

    @Override
    public List<PostEntity> readPosts() {
        return postRepository.findAllByCategoryAndIsdeletedFalse(Category.FREE);
    }
}
