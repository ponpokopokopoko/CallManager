// src/main/java/com/example/yourproject/auth/dto/LoginRequest.java
package com.example.demo.auth;

import jakarta.validation.constraints.NotBlank; // 例: バリデーション用のアノテーション
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "ユーザー名は必須です") // @Validと組み合わせて入力チェック
    private String username;

    @NotBlank(message = "パスワードは必須です")
    private String password;

}