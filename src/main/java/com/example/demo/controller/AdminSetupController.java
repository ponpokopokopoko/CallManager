package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminSetupController {

	@GetMapping("/admin-setup")
	public String adminSetupPage() {
		return "admin-setup"; // resources/templates/general-menu を返す（Thymeleaf前提）
	}


}
