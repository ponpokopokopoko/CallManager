// src/main/java/com/example/yourproject/auth/dto/AuthResponse.java
package com.example.demo.auth;

public class AuthResponse {
    private String token; // JWTトークン

    // 他にも、ユーザー名やロールなどをここに追加しても良い
    // private String username;
    // private List<String> roles;

    // コンストラクタ、Getter
    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    // 必要に応じてSetterも追加できるが、レスポンスDTOは不変にすることも多い
    // public void setToken(String token) { this.token = token; }
}