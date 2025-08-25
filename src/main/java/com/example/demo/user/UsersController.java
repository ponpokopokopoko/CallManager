package com.example.demo.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController//→@Controllerと@RequestBodyの足し算
@RequestMapping("/api/users")
public class UsersController {
	
	//コンストラクタインジェクション
	private final UsersService usersService;
	
	public UsersController(UsersService usersService) {
		this.usersService = usersService;
	}
	
	//ユーザー機能で必要なメソッド（API対応）
	//１：一覧取得、２，作成、３：削除、４：パスワード変更、

	@GetMapping
	//１，一覧取得（Get /api/users）
	public List<Users> getUsers(){//Jacksonはこれを適切にJSONに変換してくれるので大丈夫
		return usersService.getAllUsers();//なんでList<Users>で受け取っていいの？
	}
	
		
	//２，作成（POST/api/users）
	@PostMapping//作成
	public ResponseEntity<Users> createUsers(@RequestBody @Valid UserCreateRequest request){
		System.out.println("コントローラ起動");
		Users createdUser = usersService.createUser(request.getUsername(),request.getPassword());
		System.out.println("サービス終了");
		return new ResponseEntity<>(createdUser,HttpStatus.CREATED);
		
	}
	
	//３，削除（DELETE/api/users/{id}）
		@DeleteMapping("/{id}")
		//削除（DELETE/users/{id}）	//＜Void＞とは？→レスポンスボディでは何も返さない
		//ResponseEntity<T> の <T> の部分には、
		//HTTPレスポンスのボディとしてクライアントに返したいオブジェクトの型を指定
		public ResponseEntity<String> deleteUsers(@PathVariable Integer id){
			
			//管理者ユーザーか確認
		    if (usersService.isAdminUser(id)) {
		        return ResponseEntity
		        	    .status(HttpStatus.FORBIDDEN)
		        	    .body("このアカウントは管理者のため削除できません。");
		    }
			
			if (id == 1) {
		        return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
		    }
			
			boolean deleted = usersService.softDeleteUser(id);//論理削除メソッドを使う
			
			if(deleted) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);//ジェネリクスが空→推論できる場合は<>ダイアモンド演算子でいい
			}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				//第一引数：Bodyの中身、第二引数：HTTPステータスコード
			}	
		}
	
	//４，パスワード変更
		// --- 管理者によるパスワード変更 ---
		 // PATCH /api/users/{userId}/password
		 @PatchMapping("/{userId}/password")
		 @PreAuthorize("hasRole('ADMIN')") // ADMINロールを持つユーザーのみアクセス可能
		 public ResponseEntity<String> updatePasswordByAdmin(
		         @PathVariable Integer userId,
		         @Valid @RequestBody AdminPasswordUpdateRequest request) { // リクエストボディはPOJOで定義}

		     try {
		    	    usersService.updatePasswordByAdmin(userId, request.getNewPassword());
		    	    return ResponseEntity.ok("ユーザーのパスワードが正常に更新されました。");
		    	} catch (IllegalArgumentException e) {
		    	    // .notFound() の代わりに .status(HttpStatus.NOT_FOUND) を使う
		    	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		    	} catch (Exception e) {
		    	    // .internalServerError() の代わりに .status(HttpStatus.INTERNAL_SERVER_ERROR) を使う
		    	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("パスワード更新中にエラーが発生しました。");
		    	}
		 }
	
  
}
