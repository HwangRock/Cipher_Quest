package com.example.cipherquest.utils.category;

import com.example.cipherquest.model.Category;
import com.example.cipherquest.model.PostEntity;
import com.example.cipherquest.persistence.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("event")
public class EventCategoryReadStrategy implements CategoryReadStrategy {

    @Autowired
    private PostRepository postRepository;

    @Override
    public Category getCategory() {
        return Category.EVENT;
    }

    @Override
    public List<PostEntity> readPosts() {
        return postRepository.findAllByCategoryAndIsdeletedFalse(Category.EVENT);
    }

    @Override
    public Page<PostEntity> readPostsPaged(Pageable pageable) {
        return postRepository.findAllByCategoryAndIsdeletedFalse(Category.EVENT, pageable);
    }
}
