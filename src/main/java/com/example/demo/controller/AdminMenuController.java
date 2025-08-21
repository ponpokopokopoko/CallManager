package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminMenuController {

	@GetMapping("/admin-menu")
	public String adminMenuPage() {
		return "admin-menu";
	}


}
