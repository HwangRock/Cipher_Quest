package com.example.cipherquest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrawlResponseDTO {
    private String crawlText;//테스트 용
    private String sourceText;
    private String key;//테스트 용
    private String encryptedText;
}
