package com.example.demo.userAnalysis;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.UsersService;

@RestController
public class UserAnalysisController {

	//機能：フロントに個人分析データを送る機能だけ
	private final UserAnalysisService userAnalysisService;
	private final UsersService usersService;
	
	public UserAnalysisController(UserAnalysisService userAnalysisService ,UsersService usersService) {
		this.userAnalysisService =  userAnalysisService;
		this.usersService= usersService;
	}
	
	
	//フロントに個人分析データを送る
	@GetMapping("/api/userAnalysis")
	public ResponseEntity<AnalysisResponseDto> getUserAnalysis(Authentication authentication) {
		   
		    // 認証情報からユーザー名やIDを取得
		    String username = authentication.getName(); // principalのユーザー名
		    
		    Integer userId = usersService.findUserIdByUsername(username); // 実装次第！

		    
	    try {
	    	System.out.print("集計を実行:"+ userId);
	    	
	        // 集計を実行（DB保存も含む）
	        userAnalysisService.saveUserAnalysis(userId);
	        System.out.print("集計完了→集計を取得");
	        // DTOを取得して返す
	        AnalysisResponseDto analysis = userAnalysisService.getUserAnalysis(userId);
	        System.out.print("success");//データ無い場合ここ
	        return ResponseEntity.ok(analysis); // 200 OK
	    } catch (NoSuchElementException e) {
	        // ユーザーが存在しないなどのケース
	    	System.out.print("not_exist");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    } catch (Exception e) {
	        // 想定外のエラー
	    	System.out.print("un_expected");//データある場合ここが起きている
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
}
