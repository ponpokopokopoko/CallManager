package com.example.demo.auth;
//src/main/java/com/example/yourproject/auth/controller/AuthController.java


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

@Autowired
 private final AuthService authService; // AuthServiceをDIで受け取る

 public AuthController(AuthService authService) {
     this.authService = authService;
 }
 
 
 
 //必要機能：１，ログイン、２，ログアウト
 
 /*
//１，ログイン
 @PostMapping("/api/login")
 public ResponseEntity<AuthResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
     // 認証ロジックはサービス層に委譲
     AuthResponse authResponse = authService.authenticateAndGenerateToken(loginRequest);
     return ResponseEntity.ok(authResponse);
 }

 //２，ログアウト
 @PostMapping("/api/logout")
 public ResponseEntity<String> logoutUser() {
     authService.logout(); // サービス層にログアウト処理を委譲
     return ResponseEntity.ok("ログアウトしました。");
 }*/
 

 
}

