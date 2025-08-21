package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//データ入力画面
@Controller
public class RecordsController {
	@GetMapping("/general-menu/records")
	public String getRankingPage(@RequestParam("accountId") String accountId, Model model,Authentication auth) {
		model.addAttribute("accountId", accountId);
		System.out.println("ログイン中ユーザー名：" + auth.getName());
		return "records";
		
	}

	
}

