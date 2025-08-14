package com.example.demo.user;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "users")
@Data // getter,setter,toString,equals,hashCode,引数なしコンストラクタ等を自動生成
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;// 主キーなのでIntegerでOK
	
	
	@Column(name = "user_name" ,nullable = false, unique = true)
	@NotBlank// nullでも空白でもないことをアプリケーション層でバリデーション
	private String userName;
	
	@Column(name = "password_hash", nullable = false)// 拡張性のため updatable = false を削除
	private String passwordHash;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "user_role" , nullable = false )
	private String userRole;
	
	
    // @PrePersistメソッドがprotectedである理由
    // そもそもこの@PrePersistメソッドはJPA（Hibernate）が内部的に呼び出す
    // →まず、publicでは意図しない挙動が怖いのでダメ（publicの必要はない）
    // →将来的な拡張性（継承等に備え）サブクラスからオーバーライド出来るprotected修飾子にする
	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}
	
	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted;
	//null許容しないのでBoolean（参照型）ではなくboolean(プリミティブ型)
	
	public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.userRole);
    }
	
}
