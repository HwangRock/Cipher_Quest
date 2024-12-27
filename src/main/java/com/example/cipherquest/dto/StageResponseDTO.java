package com.example.cipherquest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StageResponseDTO {
    private String stageId; //현재 스테이지의 id
    private String answerText; //실제평문
    private String encryptedText; //암호문
    private String hint; //힌트
    private boolean isCorrect; //정답 여부
    private boolean retry; //재시도 여부
    private String message; //성공이나 실패했을때 메시지
}
