package com.example.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.user.Users;
import com.example.demo.user.UsersService;

@Controller
public class AttendanceFormController {
	
	private final UsersService usersService;
	public AttendanceFormController(UsersService usersService) {
		this.usersService = usersService;
	}

	@GetMapping("/general-menu/attendance")
	public String attendancePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		Users user = usersService.findByUsername(userDetails.getUsername()).get();
	    model.addAttribute("user", user);
		return "attendance-form"; // resources/templates/attendanceを返す（Thymeleaf前提）
	}

	/*@GetMapping("/your-page")
	public String showPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
	    Users user = usersService.findByUsername(userDetails.getUsername());
	    model.addAttribute("user", user);
	    return "your-template-name";
	}*/

}
