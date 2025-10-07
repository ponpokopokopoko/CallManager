package com.example.demo.salesRecords;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;


//通話結果のデータを記録するテーブル
@Entity
@Table(name = "sales_records")
@Data
public class SalesRecords {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "record_id", updatable = false)
	private Integer recordId;
	
	@Column(name = "user_id", nullable = false) private Integer userId;
	@Column(name = "account_id", nullable = false) private Integer accountId;
	@Column(name = "call_time", nullable = false) private LocalDateTime callTime;
	@Column(name = "telephoneNumber", nullable = false) private String telephone_number;
	//参照テーブル→statusパッケージ
	@Column(name = "status", nullable = false) private Integer status;	
	//参照テーブル→resultパッケージ
	@Column(name = "result", nullable = false) private Integer result;
	@Column(name = "appo_time", nullable = true) private LocalDateTime appoTime;
	//参照テーブル→resultDetailパッケージ
	@Column(name = "result_detail", nullable = false)	private Integer resultDetail;
	@Column(name = "recontact_date", nullable = true)	private LocalDateTime recontactdate;
	//ENUMあり(対応者が本人か、誰でもかの2択)
	@Enumerated(EnumType.STRING) // ← これが重要！！
	@Column(name = "handler", nullable = true) private HandlerType handler;
	@Column(name = "benefits", nullable = true)	private Boolean benefits;
	@Column(name = "benefits_targets_check", nullable = true)	private Boolean benefitsTargetsCheck;
	@Column(name = "is_deleted", nullable = false)	private boolean isDeleted;
	//null許容しないのでBoolean（参照型）ではなくboolean(プリミティブ型)
	@Column(name = "created_at",updatable = false)	private LocalDateTime createdAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}
	
	/*
	record_id	データ識別ID	INTEGER	　　　×
	user_id	ユーザID	INTEGER	　　　×
	account_id	アカウントID	INTEGER	　　　×
	call_time	通話日時	DATETIME	　　　×
	telephone_number	電話番号	VARCHAR(256)	　　　×
	statas	ステータス	INTEGER	　　　×
	result	通話結果	INTEGER	　　　×
	appo_time	アポ日時	DATETIME	　　　〇
	result_detail	結果詳細	INTEGER	　　　〇
	recontact_data	再連絡日付	DATETIME	　　　〇
	recontact_time	再連絡時間	INTEGER	　　　〇
	handler	対応者	INTEGER	　　　〇
	benefits	特典有無	BOOLEAN	　　　〇
	benefits_targets_check	特典対象チェック	BOOLEAN	　　　〇*/

}
