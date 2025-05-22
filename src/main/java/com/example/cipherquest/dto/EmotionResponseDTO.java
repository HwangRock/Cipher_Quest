package com.example.cipherquest.dto;

import com.example.cipherquest.model.EmotionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmotionResponseDTO {
    private long postId;
    private EmotionType emotionType;
    private long emotionCount;
}
