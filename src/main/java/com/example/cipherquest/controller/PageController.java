package com.example.cipherquest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
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
}

