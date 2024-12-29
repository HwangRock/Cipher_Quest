package com.example.cipherquest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String homePage() {
        return "redirect:/index.html"; // static/index.html 파일로 리다이렉트
    }
}

