package com.example.cipherquest.service;

import com.example.cipherquest.dto.CommentLikeResponseDTO;
import com.example.cipherquest.dto.CreateCommentRequestDTO;
import com.example.cipherquest.model.CommentEntity;
import com.example.cipherquest.model.CommentLikeEntity;
import com.example.cipherquest.model.PostEntity;
import com.example.cipherquest.model.UserEntity;
import com.example.cipherquest.persistence.CommentLikeRepository;
import com.example.cipherquest.persistence.CommentRepository;
import com.example.cipherquest.persistence.PostRepository;
import com.example.cipherquest.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CommentService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    public CommentEntity createComment(CreateCommentRequestDTO request, String userId){
        long postId=request.getPostId();

        Optional<PostEntity> requestPost=postRepository.findById(postId);
        if(requestPost.isEmpty()){
            throw new RuntimeException("게시물이 없음");
        }
        PostEntity post=requestPost.get();

        Optional<UserEntity> requestUser=userRepository.findByUserid(userId);
        if(requestUser.isEmpty()){
            throw new RuntimeException("토큰에 하자 있음");
        }
        UserEntity user=requestUser.get();

        if(request.getParentId()==null){
            CommentEntity response=CommentEntity.builder()
                    .postId(post)
                    .commentWriterName(user.getUsername())
                    .content(request.getContent())
                    .createdat(LocalDateTime.now())
                    .likecount(0)
                    .dislikecount(0)
                    .build();
            return commentRepository.save(response);
        }
        else{
            Optional<CommentEntity> requestParent=commentRepository.findById(request.getParentId());
            if(requestParent.isEmpty()){
                throw new RuntimeException("부모 댓글 없음");
            }
            CommentEntity parent=requestParent.get();
            if (parent.getParent()!=null) {
                throw new RuntimeException("대댓글에는 대댓글을 달 수 없음.");
            }

            CommentEntity response=CommentEntity.builder()
                    .postId(post)
                    .commentWriterName(user.getUsername())
                    .content(request.getContent())
                    .createdat(LocalDateTime.now())
                    .likecount(0)
                    .dislikecount(0)
                    .isDeleted(false)
                    .parent(parent)
                    .build();
            parent.getChildren().add(response);
            return commentRepository.save(response);
        }

    }

    public CommentEntity deleteComment(long commentId,String userId){
        Optional<UserEntity> requestUser=userRepository.findByUserid(userId);
        if(requestUser.isEmpty()){
            throw new RuntimeException("토큰에 하자 있음");
        }
        UserEntity user=requestUser.get();

        Optional<CommentEntity> requestComment=commentRepository.findById(commentId);
        if(requestComment.isEmpty()){
            throw new RuntimeException("댓글 없음");
        }
        CommentEntity comment=requestComment.get();

        if(!user.getUsername().equals(comment.getCommentWriterName())){
            throw new RuntimeException("삭제 권한 없음");
        }

        if(comment.isDeleted()){
            throw new RuntimeException("이미 삭제됨");
        }

        comment.setDeleted(true);

        return commentRepository.save(comment);
    }

    public List<CommentEntity> getCommentsForPost(long postId){
        Optional<PostEntity>postRequest=postRepository.findById(postId);
        if(postRequest.isEmpty()){
            throw new RuntimeException("게시물 없음");
        }

        PostEntity post=postRequest.get();
        List<CommentEntity>allComments=commentRepository.findAllByPostIdAndIsDeletedFalse(post);
        List<CommentEntity>response=new ArrayList<>();

        Map<Long,CommentEntity>map= new HashMap<>();
        for (CommentEntity comment : allComments) {
            map.put(comment.getId(), comment);
        }

        for (CommentEntity comment : allComments) {
            if (comment.getParent() != null) {
                map.get(comment.getParent().getId()).getChildren().add(map.get(comment.getId()));
            }
            else {
                response.add(map.get(comment.getId()));
            }
        }

        return response;
    }

    public CommentLikeResponseDTO commentLike(String userId, long commentId){
        Optional<UserEntity> requestUser=userRepository.findByUserid(userId);
        if(requestUser.isEmpty()){
            throw new RuntimeException("토큰에 하자 있음");
        }
        UserEntity user=requestUser.get();

        Optional<CommentEntity> requestComment=commentRepository.findById(commentId);
        if(requestComment.isEmpty()){
            throw new RuntimeException("댓글 없음");
        }
        CommentEntity comment=requestComment.get();

        if(user.getUsername().equals(comment.getCommentWriterName())){
            throw new RuntimeException("본인 댓글에 좋아요 못누름");
        }

        Optional<CommentLikeEntity> like=commentLikeRepository.findByUserAndComment(user,comment);
        if(like.isEmpty()){
            CommentLikeEntity newLike = CommentLikeEntity.builder()
                    .user(user)
                    .comment(comment)
                    .likedAt(LocalDateTime.now())
                    .build();

            commentLikeRepository.save(newLike);
            comment.setLikecount(comment.getLikecount()+1);
        }
        else{
            CommentLikeEntity existingLike = like.get();
            commentLikeRepository.delete(existingLike);
            comment.setLikecount(comment.getLikecount()-1);
        }
        commentRepository.save(comment);

        CommentLikeResponseDTO response = CommentLikeResponseDTO.builder()
                .commentId(commentId)
                .likeCount(comment.getLikecount())
                .build();

        return response;
    }

}
