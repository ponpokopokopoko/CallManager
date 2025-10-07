package com.example.demo.userAnalysis;

//集計したい「指標の種類」をまとめて定義した Enum
public enum MetricType {
	//新規
	NEW_CALL_COUNT,//通話件数
	NEW_APPO_COUNT,//アポ件数
    NEW_APPO_RATE,//アポ率
    
    //再交渉
    RENEGO_CALL_COUNT,//通話件数
    RENEGO_APPO_COUNT,//アポ件数
    RENEGO_APPO_RATE,//アポ率
    
    //全通話
  	TOTAL_CALL_COUNT,//全通話数
  	TOTAL_APPO_COUNT,//全通話アポ数
  	TOTAL_APPO_RATE,//全通話アポ率
  	
    //負け理由
	LOST_INTRO_COUNT,//導入負け件数
	LOST_INTRO_RATE,//導入負け率
	LOST_HEARING_COUNT,//ヒアリング負け件数
	LOST_HEARING_RATE,//ヒアリング負け率
	LOST_PROPOSAL_COUNT,//提案負け件数
	LOST_PROPOSAL_RATE,//提案負け率
	INVALID_COUNT,//非有効件数
	INVALID_RATE, //非有効率

}
    