package com.allieat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BackStageController {
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:backstage_login.html";
    }
}
