package com.example.demo.userAnalysis;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

//分析結果のレスポンスに使うDTO
@AllArgsConstructor
@Data
public class AnalysisResponseDto {
    
	private int statusCode;
    private Integer userId;
    private LocalDateTime generatedAt;

    private PeriodSummary today;
    private PeriodSummary thisWeek;
    private PeriodSummary thisMonth;
}
