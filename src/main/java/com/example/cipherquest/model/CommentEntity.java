package com.example.cipherquest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity postId;

    @ManyToOne
    @JoinColumn(name = "parent_id") // 일반 댓글이면 null을 가짐.
    private CommentEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<CommentEntity> children = new ArrayList<>();

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String commentWriterName; // 애당초 바꾸지 못하게 하는게 좋지만 유저가 이름을 변경한다면? 을 고민해봐야함.

    @Column()
    private long likecount;

    @Column()
    private long dislikecount;

    @Column()
    private boolean isDeleted=false;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdat;

}
