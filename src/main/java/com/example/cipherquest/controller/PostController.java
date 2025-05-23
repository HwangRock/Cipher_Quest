package com.example.cipherquest.controller;

import com.example.cipherquest.dto.*;
import com.example.cipherquest.model.PostEntity;
import com.example.cipherquest.service.JwtProvider;
import com.example.cipherquest.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/createPost")
    public ResponseEntity<?> createPost(@RequestHeader("Authorization") String authHeader,
                                        @RequestBody CreatePostRequestDTO request){
        String userid=jwtProvider.extractUserId(authHeader);

        PostEntity posted=postService.createPost(request,userid);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .isSuccess(true)
                        .responseDto(posted)
                        .build()
        );
    }

    @GetMapping("/readPost/{postId}")
    public ResponseEntity<?> readPost(@PathVariable Long postid) {
        ReadPostResponseDTO response=postService.readPost(postid);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .isSuccess(true)
                        .responseDto(response)
                        .build()
        );
    }

    @PatchMapping("/updatePost/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postid,@RequestBody UpdatePostRequestDTO request,
                                        @RequestHeader("Authorization") String authHeader){
        String userid=jwtProvider.extractUserId(authHeader);
        PostEntity updated=postService.updatePost(request,userid,postid);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .isSuccess(true)
                        .responseDto(updated)
                        .build()
        );
    }

    @PatchMapping("/deletePost/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postid, @RequestHeader("Authorization") String authHeader){
        String userid=jwtProvider.extractUserId(authHeader);

        PostEntity LogicallyDeleted=postService.LogicallyDeletePost(postid,userid);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .isSuccess(true)
                        .responseDto(LogicallyDeleted)
                        .build()
        );
    }

    @GetMapping("/readCategory/{category}")
    public ResponseEntity<?> readCategory(@PathVariable String categoty,@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size){
        Page<PostEntity> response=postService.readCategory(categoty,page,size);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .isSuccess(true)
                        .responseDto(response)
                        .build()
        );
    }

    @PostMapping("/emotePost")
    public ResponseEntity<?> emotePost(@RequestHeader("Authorization") String authHeader,
                                       @RequestBody EmotionRequestDTO request){
        String userid=jwtProvider.extractUserId(authHeader);
        EmotionResponseDTO response=postService.postEmote(userid,request);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .isSuccess(true)
                        .responseDto(response)
                        .build()
        );
    }
}
