package com.example.cipherquest.service;

import com.example.cipherquest.dto.CreatePostRequestDTO;
import com.example.cipherquest.dto.ReadPostRequestDTO;
import com.example.cipherquest.dto.ReadPostResponseDTO;
import com.example.cipherquest.model.Category;
import com.example.cipherquest.model.PostEntity;
import com.example.cipherquest.model.UserEntity;
import com.example.cipherquest.persistence.PostRepository;
import com.example.cipherquest.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public PostEntity createPost(CreatePostRequestDTO request, String userid){
        String title=request.getTitle();
        String content=request.getContent();
        Category category=request.getCategory();

        Optional<UserEntity> writer=userRepository.findByUserid(userid);
        if(writer.isEmpty()){
            throw new RuntimeException("토큰에 하자 있음");
        }

        if(title.length()<3){
            throw new RuntimeException("제목은 3자 이상이어야 합니다.");
        }
        if(content.length()<10){
            throw new RuntimeException("내용은 10자 이상이어야 합니다.");
        }

        PostEntity posting=PostEntity.builder()
                .category(category)
                .content(content)
                .title(title)
                .createdat(LocalDateTime.now())
                .updateat(LocalDateTime.now())
                .likecount(0)
                .dislikecount(0)
                .isdeleted(false)
                .writer(writer.get())
                .build();

        return postRepository.save(posting);

    }

    public ReadPostResponseDTO readPost(long postid){
        Optional<PostEntity> post=postRepository.findById(postid);
        if(post.isEmpty()){
            throw new RuntimeException("잘못된 게시물입니다.");
        }

        ReadPostResponseDTO response=ReadPostResponseDTO.builder()
                .category(post.get().getCategory())
                .updateat(post.get().getUpdateat())
                .createdat(post.get().getCreatedat())
                .content(post.get().getContent())
                .title(post.get().getTitle())
                .likecount(post.get().getLikecount())
                .dislikecount(post.get().getDislikecount())
                .writername(post.get().getWriter().getUsername())
                .build();

        return response;
    }
}
