package com.example.demo.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.user.UsersRepository;

//src/main/java/com/example/yourproject/auth/service/AuthService.java



@Service // Springのサービスコンポーネントとして登録
public class AuthService {

 
 private final UsersRepository usersRepository;
 private final PasswordEncoder passwordEncoder; 
 // UserDetailsServiceはAuthenticationManagerが内部で利用するため、直接注入は不要な場合が多い
 // 必要であれば注入しても良いが、ここでは省略
 

 public AuthService(UsersRepository usersRepository,PasswordEncoder passwordEncoder) {
     
     this.usersRepository = usersRepository;
     this.passwordEncoder = passwordEncoder;
 }
 
 
 //必要なロジック
 //１，ログイン、2、ログアウト。3トークン作成

 /*
//１，ログイン機能
	public Optional<Users> loginUser(String userName,String rawPassword){
		//ログインしようとしているユーザーが存在しているか確認
		Optional<Users> optionalUser = usersRepository.findByUserNameAndIsDeletedFalse(userName);
		
		if(optionalUser.isPresent()) {
			Users user = optionalUser.get();
			// 2. パスワードの照合
           // BCryptPasswordEncoderなどのPasswordEncoderを使って照合する
           if (passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
               // 3. 認証成功
               // ユーザーが論理削除されていないかも確認すると良いでしょう
               if (!user.isDeleted()) { // isDeletedがfalseの場合のみログイン許可
                   return Optional.of(user); // 認証成功したユーザーを返す
               }
           }
		}
        // 認証失敗
        return Optional.empty(); // ユーザーが見つからない、またはパスワード不一致
	}*/

	//2,ログアウト
 // ログアウト処理など、認証に関する他のビジネスロジックもここに追加
 public void logout() {
     // 例えば、リフレッシュトークンの無効化など、サーバーサイドでのログアウト処理
     // JWT自体はステートレスなので、通常はクライアント側でトークンを破棄するよう促す
 }
 
 
 
}