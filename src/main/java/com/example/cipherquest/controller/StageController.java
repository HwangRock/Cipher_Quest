package com.example.cipherquest.controller;

import com.example.cipherquest.dto.ResponseDTO;
import com.example.cipherquest.dto.StageRequestDTO;
import com.example.cipherquest.dto.StageResponseDTO;
import com.example.cipherquest.service.EncryptService;
import com.example.cipherquest.service.StageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.ok().body("save to redis");
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
                plainText=null;
            }
            else{
                plainText=txt.toString();
            }

            StageResponseDTO stageResponseDTO = StageResponseDTO.builder()
                    .stageId(id)
                    .answerText(plainText)
                    .build();
            return ResponseEntity.ok().body(stageResponseDTO);
        } catch (RuntimeException e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
