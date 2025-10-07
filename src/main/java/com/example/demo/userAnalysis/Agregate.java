package com.example.demo.userAnalysis;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

//それぞれのユーザーの分析結果を抱えるテーブル
@Entity
@Data
public class Agregate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer agregateId; // 集計ID（主キー）
    
    
    @Column(nullable = false) private Integer userId; // ユーザID
    @Column(nullable = false) private LocalDate summaryDate; // 集計日
    @Column(nullable = false) private LocalDateTime lastUpdatedAt; // 最終更新時間

    // Count
    @Column(nullable = false) private int newCallCount; // 新規架電件数
    @Column(nullable = false) private int newAppoCount; // 新規アポ件数    
    
    @Column(nullable = false) private int renegoCallCount; // 再交渉架電件数
    @Column(nullable = false) private int renegoAppoCount; // 再交渉アポ件数    
    @Column(nullable = false) private int lostIntroCount; // 導入負け件数	
    @Column(nullable = false) private int lostHearingCount; // ヒアリング負け件数	
    @Column(nullable = false) private int lostProposalCount; // 提案負け件数	
    @Column(nullable = false) private int invalidCount; // 非有効件数
        
    @Column(nullable = false) private int totalCallCount; // 総通話件数
    @Column(nullable = false) private int totalAppoCount; // 総アポ数
    @Column(nullable = false) private double totalAppoRate; // 総アポ率
 
    
    // Rate  
    @Column(nullable = false) private double newAppoRate; // 新規アポ率
    @Column(nullable = false) private double renegoAppoRate; // 再交渉アポ率
    @Column(nullable = false) private double lostIntroRate; // 導入負け率
    @Column(nullable = false) private double lostHearingRate; // ヒアリング負け率
    @Column(nullable = false) private double lostProposalRate; // 提案負け率
    @Column(nullable = false) private double invalidRate; // 非有効率
}
