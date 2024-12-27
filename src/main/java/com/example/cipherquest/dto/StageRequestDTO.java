package com.example.cipherquest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StageRequestDTO {
    private String stageId; // 현재 스테이지의 ID
    private String submitDecryptText; // 사용자가 제출한 복호화 문장
    private String submitPlainText; // 사용자가 암호화되길 바라는 문장
    private int key; // 암호화/복호화 키
    private String nextStageId; // 다음 스테이지의 ID
}
