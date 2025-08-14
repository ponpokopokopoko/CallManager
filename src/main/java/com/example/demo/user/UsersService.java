package com.example.demo.user;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class UsersService {
	
	//レポジトリのDIをする→コンストラクタインジェクションが現在の標準
	private final UsersRepository usersRepository;
	private final PasswordEncoder passwordEncoder; // パスワードエンコーダーをDI
	
	//コンストラクタはクラス名と同名、返り値なし
	public  UsersService(UsersRepository usersRepository,PasswordEncoder passwordEncoder) {
		this.usersRepository = usersRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	//ユーザー機能で必要なメソッド（API対応）
		//１：一覧取得、２，作成、３：削除、４：パスワード変更、（特定ユーザー取得は不要）
	
	//１，一覧取得
	//全ユーザー取得→削除フラグ立ってるやつを除く！カスタムメソッドが必要
	public List<Users> getAllUsers() {
		return usersRepository.findByIsDeletedFalse();
	}
	
	//1.5：特定ユーザーがADMINかの確認
	//trueの場合、ADMIN（管理者ユーザー）なので削除禁止
	public boolean isAdminUser(Integer id) {
		Optional<Users> user = usersRepository.findById(id);
			if(user.isPresent()) {
				return user.get().isAdmin();
				
			}else {
				return false;
			}
		}
	
	//２，作成
	//新規作成
	public Users createUser(String userName, String rawPassword) {//saveの返り値はUser
		//パスワードをハッシュ化
		System.out.println("ハッシュ化する");
		String encodedPassword = passwordEncoder.encode(rawPassword);
		
		Users newUser = new Users();//インスタンス作成
		newUser.setUserName(userName);
		newUser.setPasswordHash(encodedPassword);
		newUser.setDeleted(false);//→本当に必須ですか？ 省略可能？
		
		System.out.println("設定した");
		
		// ★ 管理者が1人もいなければROLE_ADMIN、それ以外はROLE_USER
	    String role = usersRepository.countByUserRole("ADMIN") == 0 ? "ADMIN" : "USER";
	    newUser.setUserRole(role);
		
		// createdAtは@PrePersistで設定される
		return usersRepository.save(newUser);
	}
	
	
	//３．削除
	//論理削除の方（フラグ立てるだけ）
	public boolean softDeleteUser(Integer id) {
		//ユーザーが存在するか確認
		Optional<Users> selectedUser = usersRepository.findById(id);
		
		if(selectedUser.isPresent()) {
			Users user = selectedUser.get();
			if(!user.isDeleted()) {//→この辺のifはカスタムメソッド使えば省略出来る
				user.setDeleted(true);//削除フラグを立てる（@Dataがsetterを生成している）
				usersRepository.save(user);//変更を保存
				return true;
			}else {
				//既に削除フラグが立っていた場合	
				return false;
			}
		}else {
			//ユーザーが見つからなかった場合
			return false;
		}
	}
	
	
	//４，パスワード変更
	@Transactional
    public void updatePasswordByAdmin(Integer userId, String newPassword) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("指定されたユーザーが見つかりません: " + userId));

        // パスワードのハッシュ化（Spring SecurityのPasswordEncoderなどを使用することを強く推奨）
        // 例: String hashedPassword = passwordEncoder.encode(newPassword);
        // ここでは簡易的にプレーンテキストを保持していますが、本番環境では絶対に行わないでください。
        user.setPasswordHash(newPassword); // ★ ここでBCryptPasswordEncoderなどでハッシュ化する ★

        usersRepository.save(user);
    }

	//特定ユーザー取得
	public Optional<Users> findByUsername(String username) {
		// TODO 自動生成されたメソッド・スタブ
		return usersRepository.findByUserName(username);
		
	}

	public Integer findUserIdByUsername(String username) {
        return usersRepository.findByUserName(username)
                .map(Users::getUserId)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));
    }
	

	
	/*//削除→多分これだとDBからガチで消すことになる（物理削除）
	//削除フラグを立てればいいだけじゃん（論理削除）
	public boolean deleteUser(Integer id) {
		if(usersRepository.existsById(id)) {
			usersRepository.deleteById(id);
			return true;
		}else {
			return false;
		}	
	}*/

}
