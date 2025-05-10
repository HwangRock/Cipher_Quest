package com.example.cipherquest.controller;

import com.example.cipherquest.dto.CreateCommentRequestDTO;
import com.example.cipherquest.dto.ResponseDTO;
import com.example.cipherquest.model.CommentEntity;
import com.example.cipherquest.service.CommentService;
import com.example.cipherquest.service.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/creatComment")
    public ResponseEntity<?> createComment(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody CreateCommentRequestDTO request){
        String userId=jwtProvider.extractUserId(authHeader);

        CommentEntity comment=commentService.createComment(request,userId);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .isSuccess(true)
                        .responseDto(comment)
                        .build()
        );
    }
}
