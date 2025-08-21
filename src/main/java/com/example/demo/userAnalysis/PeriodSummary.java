package com.example.demo.userAnalysis;

import lombok.AllArgsConstructor;
import lombok.Data;


//PeriodSummary：架電結果の集計に使う型（時期やユーザーの情報は持たない）
//AgregateはPeriodSummaryを時期やユーザーに紐づける上位概念

@AllArgsConstructor
@Data
public class PeriodSummary {
    
	//Count
    private final int newCallCount;	//新規架電件数
    private final int newAppoCount;	//新規アポ件数    
    private final int renegoCallCount;	//再交渉架電件数
    private final int renegoAppoCount;	//再交渉アポ件数    
	private final int lostIntroCount;	//導入負け件数	
	private final int lostHearingCount;	//ヒアリング負け件数	
	private final int lostProposalCount;	//提案負け件数	
	private final int invalidCount;	//非有効件数
	
	//全通話
	private final int totalCallCount;//全通話数
	private final int totalAppoCount;//全通話アポ数
	private final double totalAppoRate; //全通話アポ率
	
	//Rate（値の％表示や丸めはフロントでやる）
	private final double newAppoRate;//新規アポ率
	private final double renegoAppoRate; //再交渉アポ率
	private final double lostIntroRate;//導入負け率
	private final double lostHearingRate;//ヒアリング負け率
	private final double lostProposalRate;//提案負け率
	private final double invalidRate;//非有効率
	
	
   }
