package com.example.cipherquest.controller;

import com.example.cipherquest.dto.ResponseDTO;
import com.example.cipherquest.dto.StageRequestDTO;
import com.example.cipherquest.dto.StageResponseDTO;
import com.example.cipherquest.service.EncryptService;
import com.example.cipherquest.service.StageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

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
            String key=requestDTO.getKey();
            if (key == null || key.isEmpty()) {
                return ResponseEntity.badRequest().body("key가 비어 있습니다.");
            }
            if(stageService.checkKey(key)){
                stageService.setToRedisWithTTL(requestDTO.getStageId(), requestDTO.getSubmitPlainText(), DURATION_TIME);
                return ResponseEntity.ok().body("save to redis: plain text");
            }
            else{
                return ResponseEntity.badRequest().body("key가 적절하지 않습니다.");
            }
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
            String txt = requestDTO.getSubmitPlainText();
            if (txt == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "평문이 없습니다."));
            }

            String k = requestDTO.getKey();
            if (k == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "키가 없습니다."));
            }

            String encrypted = encryptService.Encrypt(id, txt, k);
            log.info("Redis 저장 준비: 평문 Key = {}, Value = {}", id, txt);
            log.info("Redis 저장 준비: 암호문 Key = {}, Value = {}", id + "_enc", encrypted);

            stageService.setToRedisWithTTL(id, txt, DURATION_TIME);
            stageService.setToRedisWithTTL(id+"_enc", encrypted, DURATION_TIME);
            return ResponseEntity.ok(Map.of("encryptedText", encrypted));
        } catch (IllegalArgumentException e) {
            // 잘못된 입력 예외 처리
            return ResponseEntity.badRequest().body(Map.of("error", "잘못된 입력: " + e.getMessage()));
        } catch (RuntimeException e) {
            // 일반적인 예외 처리
            return ResponseEntity.status(500).body(Map.of("error", "서버 오류: " + e.getMessage()));
        }
    }


    @PostMapping("{id}/submit")
    public ResponseEntity<?> submit(@RequestBody StageRequestDTO reuestDTO) {
        try{
            String stageId = reuestDTO.getStageId();
            String saveKey=stageId+"_submit";
            Object submitUserText=reuestDTO.getSubmitDecryptText();
            stageService.setToRedisWithTTL(saveKey, submitUserText, DURATION_TIME);
            log.info("submit:"+saveKey+" sava data:"+submitUserText);
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

            log.info("verify:"+findKey);
            Object answerData = stageService.getFromRedis(id);
            String answer;
            if (answerData == null) {
                log.info("평문 ㄴㄴ");
                return ResponseEntity.badRequest().body(
                        ResponseDTO.builder().error("해당 스테이지 ID에 대한 평문 데이터가 없습니다.").build()
                );
            }
            answer=answerData.toString();

            Object submitData = stageService.getFromRedis(findKey);
            String submit;
            if (submitData == null) {
                log.info("제출 ㄴㄴ");
                return ResponseEntity.badRequest().body(
                        ResponseDTO.builder().error("해당 스테이지 ID에 대한 제출 데이터가 없습니다.").build()
                );
            }
            submit=submitData.toString();

            stageService.deleteFromRedis(findKey);

            if (submit.equals(answer)) {
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

    @GetMapping("{Id}/cipher")
    public ResponseEntity<?> getCipher(@PathVariable("Id") String id){
        try{
            String key=id+"_enc";
            Object cipherText=stageService.getFromRedis(key);
            if(cipherText==null){
                return ResponseEntity.badRequest().body(
                        StageResponseDTO.builder().message("암호문을 찾을 수 없습니다.").build()
                );
            }
            StageResponseDTO stageResponseDTO=StageResponseDTO.builder()
                    .stageId(id)
                    .encryptedText(cipherText.toString()).build();

            return ResponseEntity.ok().body(stageResponseDTO);
        }
        catch(RuntimeException e) {
            return ResponseEntity.internalServerError().body(
                    ResponseDTO.builder().error("작업 중 오류가 발생했습니다: " + e.getMessage()).build()
            );
        }
    }
}
