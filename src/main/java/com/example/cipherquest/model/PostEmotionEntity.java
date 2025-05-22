package com.example.cipherquest.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "post_emotion",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"})
)
public class PostEmotionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmotionType emotionType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime emotionAt;
}
