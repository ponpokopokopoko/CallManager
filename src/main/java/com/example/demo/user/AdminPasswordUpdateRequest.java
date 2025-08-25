package com.example.demo.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


//パスワード変更リクエストのDTO
@Data
class AdminPasswordUpdateRequest {
    @NotBlank(message = "新しいパスワードは必須です。")
    private String newPassword;
    
}