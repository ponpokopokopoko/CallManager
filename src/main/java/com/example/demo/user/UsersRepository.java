package com.example.demo.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


//リポジトリ：エンティティというJavaで書かれたテーブルの設計図のオブジェクトの操作を担当
//クラスではなくインターフェース
//JpaRepositoryを継承する＜エンティティ型, 主キーの型＞
//必要に応じて自分でメソッドの定義も出来る
@Repository//Stringに対し、リポジトリコンポーネントとしてDIの対象であると伝える
public interface UsersRepository extends JpaRepository<Users, Integer>{

	
	//↓カスタムメソッド達
	
	
	// 論理削除されていない全てのユーザーを取得するカスタムメソッド
    List<Users> findByIsDeletedFalse(); // findBy + フィールド名 + False/True

    // IDを指定して、論理削除されていない特定のユーザーを取得するカスタムメソッド
    Optional<Users> findByUserIdAndIsDeletedFalse(Integer userId); // findBy + フィールド名 + And + フィールド名 + False/True

    Optional<Users> findByUserNameAndIsDeletedFalse(String userName);

    Optional<Users> findByUserName(String userName);

	int countByUserRole(String string);

	Optional<Users> findByUserId(Integer id);
	
	boolean existsByUserRole(String role);
	

}

