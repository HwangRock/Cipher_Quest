package com.example.cipherquest.controller;

import com.example.cipherquest.service.EncryptService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PageController {
    private EncryptService encryptService;
    public PageController(EncryptService encryptService) {
        this.encryptService = encryptService;
    }

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("cipherquiz")
    public String cipherquizPage() {
        return "cipherquiz";
    }

    @GetMapping("/randomquiz")
    public String randomQuizPage() {
        return "randomquiz";
    }

    @GetMapping("/studycipher")
    public String studyCipherPage() {
        return "studycipher";
    }

    @GetMapping("/quiz")
    public String quizPage(
            @RequestParam("stageId") String stageId,
            @RequestParam("plainText") String plainText,
            @RequestParam("key") String key,
            RedirectAttributes redirectAttributes) {
        // 암호화 로직 처리
        String encrypted = encryptService.Encrypt(stageId, plainText, key);

        // URL에 파라미터로 추가
        redirectAttributes.addAttribute("stageId", stageId);
        redirectAttributes.addAttribute("cipher", encrypted);

        return "redirect:/quiz/display";
    }

    @GetMapping("/quiz/display")
    public String displayQuizPage(
            @RequestParam("stageId") String stageId,
            @RequestParam("cipher") String cipher,
            Model model) {
        // 모델에 데이터 추가
        model.addAttribute("stageId", stageId);
        model.addAttribute("cipher", cipher);

        return "onquiz";
    }
}

