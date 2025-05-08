package com.example.cipherquest.dto;

import com.example.cipherquest.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadPostResponseDTO {
    private String writername;
    private String title;
    private String content;
    private LocalDateTime createdat;
    private LocalDateTime updateat;
    private long likecount;
    private long dislikecount;
    private Category category;
}
