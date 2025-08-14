package com.example.demo.common;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.user.Users;
import com.example.demo.user.UsersRepository;


// Spring SecurityのUserDetailsオブジェクトを作成して返す
//Spring Securityの活用に必要なクラス
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository; 
    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DBからユーザー情報を取得
        Users user = usersRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        
        // ここでユーザーの権限（ロール）も設定
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPasswordHash(), // DBに保存されたハッシュ化されたパスワード
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserRole())) // ← ここ大事
                //new ArrayList<>() // ユーザーの権限（例: new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}

