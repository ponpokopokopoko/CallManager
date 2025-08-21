package com.example.demo.ranking;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

//ランキング要求の返信に使うDTO
@Data
public class RankingResponseDto {
    

    private String userName; // ユーザ名
    private LocalDate summaryDate; // 集計日
    private LocalDateTime lastUpdatedAt; // 最終更新時間

    // Count
    private int newCallCount; // 新規架電件数
    private int newAppoCount; // 新規アポ件数    
    
    private int renegoCallCount; // 再交渉架電件数
    private int renegoAppoCount; // 再交渉アポ件数  
    
    private int lostIntroCount; // 導入負け件数	
    private int lostHearingCount; // ヒアリング負け件数	
    private int lostProposalCount; // 提案負け件数	
    private int invalidCount; // 非有効件数
        
    private int totalCallCount; // 総通話件数
    private int totalAppoCount; // 総アポ数
    private double totalAppoRate; // 総アポ率
 
    
    // Rate  
    private double newAppoRate; // 新規アポ率
    private double renegoAppoRate; // 再交渉アポ率
    private double lostIntroRate; // 導入負け率
    private double lostHearingRate; // ヒアリング負け率
    private double lostProposalRate; // 提案負け率
    private double invalidRate; // 非有効率
}
