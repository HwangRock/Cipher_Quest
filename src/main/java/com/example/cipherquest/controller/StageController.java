package com.example.cipherquest.controller;

import com.example.cipherquest.dto.CrawlResponseDTO;
import com.example.cipherquest.dto.ResponseDTO;
import com.example.cipherquest.dto.StageRequestDTO;
import com.example.cipherquest.dto.StageResponseDTO;
import com.example.cipherquest.service.EncryptService;
import com.example.cipherquest.service.StageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
        String key=requestDTO.getKey();
        if(stageService.checkKey(key)){
            stageService.setToRedisWithTTL(requestDTO.getStageId(), requestDTO.getSubmitPlainText(), DURATION_TIME);
            return ResponseEntity.ok().body("save to redis: plain text");
        }
        else{
            return ResponseEntity.badRequest().body("key가 적절하지 않습니다.");
        }
    }

    @PostMapping("/randomCreate")
    public ResponseEntity<?> createCrawl(@RequestBody StageRequestDTO requestDTO) throws IOException {
        String crawl[]=stageService.crawling();
        String crawlText=crawl[0];
        String source=crawl[1];

        String key=encryptService.CreateKey(requestDTO.getStageId());
        String encTxt=encryptService.Encrypt(requestDTO.getStageId(),crawlText,key);

        CrawlResponseDTO response=CrawlResponseDTO.builder()
                .crawlText(crawlText)
                .sourceText(source)
                .key(key)
                .encryptedText(encTxt)
                .build();

        if(stageService.checkKey(key)){
            stageService.setToRedisWithTTL(requestDTO.getStageId(),crawlText, DURATION_TIME);
            return ResponseEntity.ok(new ResponseDTO<>(response));
        }
        else{
            return ResponseEntity.badRequest().body("key가 적절하지 않습니다.");
        }
    }

    @GetMapping("/{id}/giveup")
    public ResponseEntity<?> getText(@PathVariable("id") String id) {
        Object txt=stageService.getFromRedis(id);
        String plainText;
        plainText=txt.toString();

        stageService.deleteFromRedis(id);
        log.info("스테이지 {} 데이터 삭제 완료", id);

        StageResponseDTO stageResponseDTO = StageResponseDTO.builder()
                .stageId(id)
                .answerText(plainText)
                .build();
        return ResponseEntity.ok().body(stageResponseDTO);
    }

    @PostMapping("/{id}/encrypt")
    public ResponseEntity<?> encrypt(@PathVariable("id") String id, @RequestBody StageRequestDTO requestDTO) {
        String txt = requestDTO.getSubmitPlainText();
        String k = requestDTO.getKey();

        String encrypted = encryptService.Encrypt(id, txt, k);
        log.info("Redis 저장 준비: 평문 Key = {}, Value = {}", id, txt);
        log.info("Redis 저장 준비: 암호문 Key = {}, Value = {}", id + "_enc", encrypted);

        stageService.setToRedisWithTTL(id, txt, DURATION_TIME);
        stageService.setToRedisWithTTL(id+"_enc", encrypted, DURATION_TIME);
        return ResponseEntity.ok(Map.of("encryptedText", encrypted));
    }


    @PostMapping("{id}/submit")
    public ResponseEntity<?> submit(@RequestBody StageRequestDTO reuestDTO) {
        String stageId = reuestDTO.getStageId();
        String saveKey=stageId+"_submit";
        Object submitUserText=reuestDTO.getSubmitDecryptText();
        stageService.setToRedisWithTTL(saveKey, submitUserText, DURATION_TIME);
        log.info("submit:"+saveKey+" sava data:"+submitUserText);
        return ResponseEntity.ok().body("save to redis: player submit text");
    }

    @GetMapping("{id}/verify")
    public ResponseEntity<?> verify(@PathVariable("id") String id) {
        String findKey = id + "_submit";

        log.info("verify:"+findKey);
        Object answerData = stageService.getFromRedis(id);
        String answer;
        answer=answerData.toString();

        Object submitData = stageService.getFromRedis(findKey);
        String submit;
        submit=submitData.toString();

        stageService.deleteFromRedis(findKey);

        if (submit.equals(answer)) {
            return ResponseEntity.ok().body(
                    StageResponseDTO.builder()
                            .isCorrect(true)
                            .message("정답입니다.")
                            .build()
            );
        }
        else {
            return ResponseEntity.ok().body(
                    StageResponseDTO.builder()
                            .isCorrect(false)
                            .message("오답입니다.")
                            .build()
            );
        }
    }

    @GetMapping("{Id}/cipher")
    public ResponseEntity<?> getCipher(@PathVariable("Id") String id){
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
}
