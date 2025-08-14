package com.example.demo.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//新規ユーザーを作成するときに必要なDTO
@Data
public class UserCreateRequest {
	
	@NotBlank(message = "ユーザー名は必須です")
	private String username;
	
	@NotBlank(message = "パスワードは必須です")
	private String password;
	
	private String userRole;

}
