package com.example.cipherquest.controller;

import com.example.cipherquest.dto.ResponseDTO;
import com.example.cipherquest.dto.StageRequestDTO;
import com.example.cipherquest.dto.StageResponseDTO;
import com.example.cipherquest.service.EncryptService;
import com.example.cipherquest.service.StageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/stage")
public class StageController {
    private static final int DURATION_TIME=216000;

    private StageService stageService;
    private EncryptService encryptService;

    public StageController(StageService stageService, EncryptService encryptService) {
        this.stageService = stageService;
        this.encryptService = encryptService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createText(@RequestBody StageRequestDTO requestDTO) {
        try{
            stageService.setToRedisWithTTL(requestDTO.getStageId(), requestDTO.getSubmitPlainText(), DURATION_TIME);
            return ResponseEntity.ok().body("save to redis: plain text");
        } catch (RuntimeException e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/{id}/giveup")
    public ResponseEntity<?> getText(@PathVariable("id") String id) {
        try{
            Object txt=stageService.getFromRedis(id);
            String plainText;
            if(txt==null){
                return ResponseEntity.badRequest().body(
                        ResponseDTO.builder().error("해당 스테이지 ID에 대한 데이터가 없습니다.").build()
                );
            }
            else{
                plainText=txt.toString();
            }

            stageService.deleteFromRedis(id);
            log.info("스테이지 {} 데이터 삭제 완료", id);

            StageResponseDTO stageResponseDTO = StageResponseDTO.builder()
                    .stageId(id)
                    .answerText(plainText)
                    .build();
            return ResponseEntity.ok().body(stageResponseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    ResponseDTO.builder().error("작업 중 오류가 발생했습니다: " + e.getMessage()).build()
            );
        }
    }

    @PostMapping("/{id}/encrypt")
    public ResponseEntity<?> encrypt(@PathVariable("id") String id, @RequestBody StageRequestDTO requestDTO) {
        try {
            Object txt = requestDTO.getSubmitPlainText();
            if (txt == null) {
                return ResponseEntity.badRequest().body("평문이 없습니다.");
            }

            Object k=requestDTO.getKey();
            if (k == null) {
                return ResponseEntity.badRequest().body("key가 없습니다.");
            }

            String encrypted= encryptService.Encrypt(id, txt.toString(), k);

            return ResponseEntity.ok().body(encrypted);

        }catch (IllegalArgumentException e) {
            // 잘못된 입력 예외 처리
            return ResponseEntity.badRequest().body(
                    ResponseDTO.builder().error("잘못된 입력: " + e.getMessage()).build()
            );
        } catch (RuntimeException e) {
            // 일반적인 예외 처리
            return ResponseEntity.status(500).body(
                    ResponseDTO.builder().error("서버 오류: " + e.getMessage()).build()
            );
        }
    }

    @PostMapping("{id}/submit")
    public ResponseEntity<?> submit(@PathVariable("id") String id, @RequestParam String submitUserText) {
        try{
            String saveKey=id+"_submit";
            stageService.setToRedisWithTTL(saveKey, submitUserText, DURATION_TIME);
            return ResponseEntity.ok().body("save to redis: player submit text");
        }catch(RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    ResponseDTO.builder().error("작업 중 오류가 발생했습니다: " + e.getMessage()).build());
        }
    }

    @GetMapping("{id}/verify")
    public ResponseEntity<?> verify(@PathVariable("id") String id) {
        try {
            String findKey = id + "_submit";
            Object answer = stageService.getFromRedis(id);
            Object submit = stageService.getFromRedis(findKey);

            if (submit == null) {
                return ResponseEntity.badRequest().body(
                        ResponseDTO.builder().error("해당 스테이지 ID에 대한 제출 데이터가 없습니다.").build()
                );
            }
            if (answer == null) {
                return ResponseEntity.badRequest().body(
                        ResponseDTO.builder().error("해당 스테이지 ID에 대한 데이터가 없습니다.").build()
                );
            }

            if (submit.toString().equals(answer.toString())) {
                return ResponseEntity.ok().body(
                        ResponseDTO.builder().data(List.of("정답입니다.")).build()
                );
            } else {
                return ResponseEntity.ok().body(
                        ResponseDTO.builder().data(List.of("오답입니다.")).build()
                );
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseDTO.builder().error("작업 중 오류가 발생했습니다: " + e.getMessage()).build()
            );
        }
    }

}
