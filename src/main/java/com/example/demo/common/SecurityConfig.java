package com.example.demo.common; 

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//SprngSecurityを導入したらこの「セキュリティ設定」クラスを作成する

@Configuration
@EnableWebSecurity // Webセキュリティを有効にする
public class SecurityConfig {
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.sessionManagement(session -> session
			        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			    )
			.csrf(csrf -> csrf.disable())
			.formLogin(form -> form
	        .loginPage("/login") // 自作ログインページ
	        .successHandler(new CustomLoginSuccessHandler()) // ロールで遷移を切り替える
	        .permitAll()
	        )
	        .logout(logout -> logout
	        .logoutUrl("/logout")
	        .logoutSuccessUrl("/login")
	        .permitAll()
	        )
	        .authorizeHttpRequests(auth -> auth
	        .requestMatchers("/","/api/users","/api/accounts","/admin-setup","/login").permitAll() // ←追加！
	        .requestMatchers("/admin-menu/**").hasRole("ADMIN")
	        .requestMatchers("/general-menu/**").hasAnyRole("ADMIN","USER")
	        .requestMatchers("/general-menu").authenticated()
	        .anyRequest().authenticated()
	      );
		
	      return http.build();
	  }

	  @Bean
	  public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
      }
}