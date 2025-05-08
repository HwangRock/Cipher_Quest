package com.example.cipherquest.dto;

import com.example.cipherquest.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequestDTO {
    private String title;
    private String content;
    private Category category;
}
