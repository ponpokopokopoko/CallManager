package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralMenuController {
	

	
	@GetMapping("/general-menu")
	public String generalMenuPage(Authentication auth) {
	    System.out.println("ログイン中ユーザー名：" + auth.getName());
	    return "general-menu";
	}
}