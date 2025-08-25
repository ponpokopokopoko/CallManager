package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.user.UsersRepository;

@Controller

public class HelloController {
	
	 private final UsersRepository usersRepository;

	    public HelloController(UsersRepository usersRepository) {
	        this.usersRepository = usersRepository;
	    }

	    @GetMapping("/")
	    public String first(Model model) {
	        boolean adminExists = usersRepository.existsByUserRole("ADMIN");

	        if (!adminExists) {
	            // ADMINがいなければ管理者セットアップ画面へ
	            return "admin-setup";
	        } else {
	            // ADMINがいれば通常ログインページへ
	            return "login"; 
	        }
	    }

}
