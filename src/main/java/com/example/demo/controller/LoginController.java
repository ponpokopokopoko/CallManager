package com.example.demo.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//ログインへ遷移
@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginPage() {
        return "login"; 
    }
}