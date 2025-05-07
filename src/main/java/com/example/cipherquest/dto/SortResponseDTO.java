package com.example.cipherquest.dto;

import com.example.cipherquest.model.Tier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SortResponseDTO {
    private String name;
    private long score;
    private Tier tier;
}
