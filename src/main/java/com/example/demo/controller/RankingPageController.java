package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//ランキング表示画面
@Controller
public class RankingPageController {
	
	@GetMapping("/general-menu/ranking")
	public String RankingPage(Authentication auth) {
	    System.out.println("ログイン中ユーザー名：" + auth.getName());
	    return "ranking";
	}
	
}
