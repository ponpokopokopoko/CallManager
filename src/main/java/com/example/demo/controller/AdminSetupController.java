package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//管理者設定画面へ遷移
@Controller
public class AdminSetupController {

	@GetMapping("/admin-setup")
	public String adminSetupPage() {
		return "admin-setup"; 
	}


}
