package com.example.demo.userAnalysis;
	
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.demo.salesRecords.SalesRecords;
import com.example.demo.salesRecords.SalesRecordsRepository;
import com.example.demo.salesRecords.contents.ResultConstants;
import com.example.demo.salesRecords.contents.ResultDetailConstants;
import com.example.demo.salesRecords.contents.StatusConstants;
	
@Service
public class UserAnalysisService {
	
		private AgregateRepository agregateRepo;
		private SalesRecordsRepository salesRecordsRepo;
		
		public UserAnalysisService (AgregateRepository agregateRepo,SalesRecordsRepository salesRecordsRepo) {
			this.agregateRepo = agregateRepo;
			this.salesRecordsRepo = salesRecordsRepo;
		}
		
	
		//１：データ集計処理→DB登録
		//２：DBから集計結果取得
	
		//フロー　情報取得→集計、加工→DB保存→DBからDto返す
	
		//１，集計処理→DB保存
		public void saveUserAnalysis(Integer userId) {
			
			System.out.println("集計処理→DB保存の開始");
			
		    // ① データ取得		
		    List<SalesRecords> records = salesRecordsRepo.findByUserIdAndIsDeletedFalse(userId)
		        .orElse(Collections.emptyList());
		    System.out.println("データ取得完了");
		    
		    /*if (records.isEmpty()) {
		    	  // データなし → 早期リターン or 空集計をDB登録しない
		    	  return;
		    	}*/
		    
		    // ② 集計用マップ初期化
		    Map<PeriodType, EnumMap<MetricType, Integer>> countMap = initCountMap();
	
		    // ③ 日付の基準値
		    LocalDate today = LocalDate.now();
		    int currentWeek = today.get(WeekFields.ISO.weekOfWeekBasedYear());
		    YearMonth thisMonth = YearMonth.from(today);
		    
	        LocalDate weekStart  = today.with(DayOfWeek.MONDAY);
	        LocalDate monthStart = today.withDayOfMonth(1);
	
		    // ④ データループ（ループ1回で全部分類）
		    //accumulate関数
		    //
		    System.out.println("データ取得start");
		    for (SalesRecords record : records) {
		    	
		        //LocalDate date = record.getCallTime().toLocalDate();//LocalDateに変更して取得
		        
		        LocalDateTime ct = record.getCallTime();
		        if (ct == null) continue;  // NPE 回避
		        LocalDate date = ct.toLocalDate();
		        System.out.println(" NPE 回避");
		        
		        	        
		        if (date.equals(today))  {
		        	System.out.println("TODAY");
		        	accumulate(countMap.get(PeriodType.TODAY),      record);
		        }
		        if (!date.isBefore(weekStart)) {
		        	System.out.println("THIS_WEEK");
		        	accumulate(countMap.get(PeriodType.THIS_WEEK),  record);
		        }
		        if (!date.isBefore(monthStart)) {
		        	System.out.println("THIS_MONTH");
		        	accumulate(countMap.get(PeriodType.THIS_MONTH), record);
		        }
		        
		        /*if (date.equals(today)) {
		            accumulate(countMap.get(PeriodType.TODAY), record);
		        }
		        if (date.get(WeekFields.ISO.weekOfWeekBasedYear()) == currentWeek) {
		            accumulate(countMap.get(PeriodType.THIS_WEEK), record);
		        }
		        if (YearMonth.from(date).equals(thisMonth)) {
		            accumulate(countMap.get(PeriodType.THIS_MONTH), record);
		        }*/
		    }
		    System.out.println("データループ完了");
		    	 
		    // 集計結果をPeriodSummaryに変換（期間ごとに）
		    PeriodSummary todaySummary = toPeriodSummary(countMap.get(PeriodType.TODAY));
		    PeriodSummary weekSummary = toPeriodSummary(countMap.get(PeriodType.THIS_WEEK));
		    PeriodSummary monthSummary = toPeriodSummary(countMap.get(PeriodType.THIS_MONTH));
		    System.out.println("集計結果をPeriodSummaryに変換完了");
	
		    // DBに登録（AgregateEntityに変換）
		    saveOrUpdateAggregate(userId, LocalDate.now(), todaySummary);
		    saveOrUpdateAggregate(userId, LocalDate.now().with(DayOfWeek.MONDAY), weekSummary);
		    saveOrUpdateAggregate(userId, LocalDate.now().withDayOfMonth(1), monthSummary);	
		    System.out.println("DBに登録（AgregateEntityに変換）完了");
		}
		
		//２，DBから取得してDTOに詰め込むロジック（nullなら集計して値回収すればいい？null出す前に集計ロジック使えばいいのでは？）
		public AnalysisResponseDto getUserAnalysis(Integer userId) {
			
			System.out.print("データ取得開始");
			
		    LocalDate today = LocalDate.now();
		    LocalDate weekStart = today.with(DayOfWeek.MONDAY);
		    LocalDate monthStart = today.withDayOfMonth(1);
	
		    Agregate todayEntity = agregateRepo.findByUserIdAndSummaryDate(userId, today).orElse(null);
		    Agregate weekEntity = agregateRepo.findByUserIdAndSummaryDate(userId, weekStart).orElse(null);
		    Agregate monthEntity = agregateRepo.findByUserIdAndSummaryDate(userId, monthStart).orElse(null);
	
		    System.out.print("データ完了→まとめます");
		    
		    return new AnalysisResponseDto(
		        200,
		        userId,
		        LocalDateTime.now(),
		        todayEntity != null ? toPeriodSummary(todayEntity) : null,
		        weekEntity != null ? toPeriodSummary(weekEntity) : null,
		        monthEntity != null ? toPeriodSummary(monthEntity) : null
		    );
		}
		
	
		
	/*以下補助関数--------------------------------------------*/
		
		
		
		//補助関数
		private Map<PeriodType, EnumMap<MetricType, Integer>> initCountMap() {
		    Map<PeriodType, EnumMap<MetricType, Integer>> map = new EnumMap<>(PeriodType.class);
		    for (PeriodType p : PeriodType.values()) {
		        map.put(p, new EnumMap<>(MetricType.class));
		        for (MetricType m : MetricType.values()) {
		            map.get(p).put(m, 0);//０初期化
		        }
		    }
		    return map;
		}
	
	
		//カウントロジック
		private void accumulate(EnumMap<MetricType, Integer> t, SalesRecords r) {
			 System.out.println("カウントロジック");
		    //int status = record.getStatus();
		    //int result = record.getResult();
		    //int resultDetail = record.getResultDetail();
		    
		    int status = r.getStatus(), result = r.getResult(),rd = r.getResultDetail();
		    
		    System.out.println("カウントロジック2");
	
		    
		    // ✅ 新規通話件数：status = NEW, 
		    if (status == StatusConstants.NEW) {
		    	t.merge(MetricType.NEW_CALL_COUNT, 1, Integer::sum);	        
		     // ✅ 新規アポ： result = APPOINTMENT
			    if (result == ResultConstants.APPOINTMENT) {
			        t.merge(MetricType.NEW_APPO_COUNT, 1, Integer::sum);}	        
		    }
		    
		 // ✅ 再交渉通話件数：status = RECONTACT,
		    if (status == StatusConstants.RECONTACT ) {
		        t.merge(MetricType.RENEGO_CALL_COUNT, 1, Integer::sum);
		        
		     // ✅ 再交渉アポ：result = APPOINTMENT
			    if (result == ResultConstants.APPOINTMENT) {
			    	t.merge(MetricType.RENEGO_APPO_COUNT, 1, Integer::sum);} 
		    }	    	 
	
		    // ✅ 導入負け：result = LOST_INTRO
		    if (result == ResultConstants.LOST_INTRO) { t.merge(MetricType.LOST_INTRO_COUNT, 1, Integer::sum);}
		    // ✅ ヒアリング負け
		    if (result == ResultConstants.LOST_HEARING) { t.merge(MetricType.LOST_HEARING_COUNT, 1, Integer::sum);}
		    // ✅ 提案負け
		    if (result == ResultConstants.LOST_PROPOSAL) { t.merge(MetricType.LOST_PROPOSAL_COUNT, 1, Integer::sum); }
	
		    // ✅ 非有効：resultDetail が特定の条件に一致
		    Set<Integer> invalidResultDetails = Set.of(
		        ResultDetailConstants.ALREADY_TALKED,
		        ResultDetailConstants.WRONG_NUMBER,
		        ResultDetailConstants.ALREADY_PURCHASED
		        
		        // 他にも無効扱いにしたいIDがあれば追加してOK！
		    );
		    if (invalidResultDetails.contains(rd)) {
		        t.merge(MetricType.INVALID_COUNT, 1, Integer::sum);
		    }
		}
	
		//PeriodSumaryに収める(カウントから比率出して全てを収める)
		private PeriodSummary toPeriodSummary(EnumMap<MetricType, Integer> countMap) {
			//全通話数
		    int totalCallCount = countMap.get(MetricType.NEW_CALL_COUNT).intValue()
		                    + countMap.get(MetricType.RENEGO_CALL_COUNT).intValue();
		    //全通話アポ数
		    int totalAppoCount = countMap.get(MetricType.NEW_APPO_COUNT).intValue()
	                + countMap.get(MetricType.RENEGO_APPO_COUNT).intValue();
		    
		  //比率//三項演算子（条件式 ? 真の場合の値 : 偽の場合の値）
		    double totalAppoRate = totalCallCount > 0 ? (double) totalAppoCount / totalCallCount : 0;//全通話アポ率
		    
		    double lostIntroRate = totalCallCount > 0 ? (double) countMap.get(MetricType.LOST_INTRO_COUNT).intValue() / totalCallCount : 0;//導入負け率
		    double lostHearingRate = totalCallCount > 0 ? (double) countMap.get(MetricType.LOST_HEARING_COUNT).intValue() / totalCallCount : 0;//ヒアリング負け率
		    double lostProposalRate = totalCallCount > 0 ? (double) countMap.get(MetricType.LOST_PROPOSAL_COUNT).intValue() / totalCallCount : 0;//提案負け率
			double newAppoRate = countMap.get(MetricType.NEW_APPO_COUNT).intValue() > 0 ? (double) countMap.get(MetricType.NEW_APPO_COUNT).intValue() / countMap.get(MetricType.NEW_CALL_COUNT).intValue() : 0 ;	//新規アポ率
			double renegoAppoRate =  countMap.get(MetricType.RENEGO_APPO_COUNT).intValue() > 0 ? (double) countMap.get(MetricType.RENEGO_APPO_COUNT).intValue() / countMap.get(MetricType.RENEGO_CALL_COUNT).intValue() : 0 ;//再交渉アポ率
			double invalidRate = totalCallCount > 0 ? (double) countMap.get(MetricType.INVALID_COUNT).intValue() / totalCallCount : 0;		//非有効率
		    
		    return new PeriodSummary(
		    	//アンボクシング（Integer→int自動変換）
		        countMap.get(MetricType.NEW_CALL_COUNT),//新規架電件数
		        countMap.get(MetricType.NEW_APPO_COUNT),//新規アポ件数
		        countMap.get(MetricType.RENEGO_CALL_COUNT),//再交渉架電件数
		        countMap.get(MetricType.RENEGO_APPO_COUNT),//再交渉アポ件数
		        countMap.get(MetricType.LOST_INTRO_COUNT),//導入負け件数	
		        countMap.get(MetricType.LOST_HEARING_COUNT),//ヒアリング負け件数	
		        countMap.get(MetricType.LOST_PROPOSAL_COUNT),//提案負け件数	
		        countMap.get(MetricType.INVALID_COUNT),//非有効件数
		        
		      //全通話
		        totalCallCount,
		        totalAppoCount,
		        totalAppoRate,
		        
		      //Rate（値の％表示や丸めはフロントでやる）
		        newAppoRate,//新規アポ率
				renegoAppoRate,//再交渉アポ率
		        lostIntroRate,//導入負け率
			    lostHearingRate,//ヒアリング負け率
			    lostProposalRate,// 提案負け率
				invalidRate//非有効率
			
		    );
		}
		
		//DBに既存の集計データがあるか確認するロジック
		private void saveOrUpdateAggregate(Integer userId, LocalDate summaryDate, PeriodSummary ps) {
		    // ① 既存の集計データがあるか探す
		    Optional<Agregate> existing = agregateRepo.findByUserIdAndSummaryDate(userId, summaryDate);
	
		    Agregate entity;
		    if (existing.isPresent()) {
		        // ② あれば更新
		        entity = existing.get();
		        updateEntityWithPeriodSummary(entity, ps); // 値を上書きする
		    } else {
		        // ③ なければ新規作成
		        entity = toEntity(userId, summaryDate, ps);
		    }
	
		    entity.setLastUpdatedAt(LocalDateTime.now());
		    agregateRepo.save(entity);
		}
	
			
		//DB新規登録ロジック
		private Agregate toEntity(Integer userId, LocalDate summaryDate, PeriodSummary ps) {
			    Agregate entity = new Agregate();
	
			    entity.setUserId(userId); // ユーザーID
			    entity.setSummaryDate(summaryDate); // 集計日
	
			    // Count
			    entity.setNewCallCount(ps.getNewCallCount());         // 新規架電件数
			    entity.setNewAppoCount(ps.getNewAppoCount());         // 新規アポ件数
			    entity.setRenegoCallCount(ps.getRenegoCallCount());   // 再交渉架電件数
			    entity.setRenegoAppoCount(ps.getRenegoAppoCount());   // 再交渉アポ件数
			    entity.setLostIntroCount(ps.getLostIntroCount());     // 導入負け件数
			    entity.setLostHearingCount(ps.getLostHearingCount()); // ヒアリング負け件数
			    entity.setLostProposalCount(ps.getLostProposalCount());// 提案負け件数
			    entity.setInvalidCount(ps.getInvalidCount());         // 非有効件数
			    entity.setTotalCallCount(ps.getTotalCallCount());	   // 総通話件数
			    entity.setTotalAppoCount(ps.getTotalAppoCount());
			    entity.setTotalAppoRate(ps.getTotalAppoRate());
	
			    // Rate
			    entity.setNewAppoRate(ps.getNewAppoRate());           // 新規アポ率
			    entity.setRenegoAppoRate(ps.getRenegoAppoRate());     // 再交渉アポ率
			    entity.setLostIntroRate(ps.getLostIntroRate());       // 導入負け率
			    entity.setLostHearingRate(ps.getLostHearingRate());   // ヒアリング負け率
			    entity.setLostProposalRate(ps.getLostProposalRate()); // 提案負け率
			    entity.setInvalidRate(ps.getInvalidRate());           // 非有効率
	
			    // 最終更新日時
			    entity.setLastUpdatedAt(LocalDateTime.now());
	
			    return entity;
		}
		//DB更新ロジック
		private void updateEntityWithPeriodSummary(Agregate entity, PeriodSummary ps) {
		    entity.setNewCallCount(ps.getNewCallCount());
		    entity.setNewAppoCount(ps.getNewAppoCount());
		    entity.setRenegoCallCount(ps.getRenegoCallCount());
		    entity.setRenegoAppoCount(ps.getRenegoAppoCount());
		    entity.setLostIntroCount(ps.getLostIntroCount());
		    entity.setLostHearingCount(ps.getLostHearingCount());
		    entity.setLostProposalCount(ps.getLostProposalCount());
		    entity.setInvalidCount(ps.getInvalidCount());
		    
		    entity.setTotalCallCount(ps.getTotalCallCount());
		    entity.setTotalAppoCount(ps.getTotalAppoCount());
		    entity.setTotalAppoRate(ps.getTotalAppoRate());
		    
		    entity.setNewAppoRate(ps.getNewAppoRate());
		    entity.setRenegoAppoRate(ps.getRenegoAppoRate());
		    entity.setLostIntroRate(ps.getLostIntroRate());
		    entity.setLostHearingRate(ps.getLostHearingRate());
		    entity.setLostProposalRate(ps.getLostProposalRate());
		    entity.setInvalidRate(ps.getInvalidRate());
		}
		
		
	
		
		//Dtoにする
		public AnalysisResponseDto fetchAggregatedSummary(Integer userId) {
		    LocalDate today = LocalDate.now();
		    LocalDate weekStart = today.with(DayOfWeek.MONDAY);
		    LocalDate monthStart = today.withDayOfMonth(1);
	
		    // DBから3つ取得
		    Agregate todayEntity = agregateRepo.findByUserIdAndSummaryDate(userId, today).orElse(null);
		    Agregate weekEntity = agregateRepo.findByUserIdAndSummaryDate(userId, weekStart).orElse(null);
		    Agregate monthEntity = agregateRepo.findByUserIdAndSummaryDate(userId, monthStart).orElse(null);
	
		    // nullチェックしてPeriodSummaryに変換
		    PeriodSummary todayPs = todayEntity != null ? toPeriodSummary(todayEntity) : null;
		    PeriodSummary thisWeekPs = weekEntity != null ? toPeriodSummary(weekEntity) : null;
		    PeriodSummary thisMonthPs = monthEntity != null ? toPeriodSummary(monthEntity) : null;
	
		    // DTO返却
		    return new AnalysisResponseDto(
		        200,
		        userId,
		        LocalDateTime.now(),
		        todayPs,
		        thisWeekPs,
		        thisMonthPs
		    );
		}
	
		private PeriodSummary toPeriodSummary(Agregate entity) {
		    return new PeriodSummary(
		        entity.getNewCallCount(),
		        entity.getNewAppoCount(),
		        entity.getRenegoCallCount(),
		        entity.getRenegoAppoCount(),
		        entity.getLostIntroCount(),
		        entity.getLostHearingCount(),
		        entity.getLostProposalCount(),
		        entity.getInvalidCount(),
		        entity.getTotalCallCount(),
		        entity.getTotalAppoCount(),
		        entity.getTotalAppoRate(),
		        entity.getNewAppoRate(),
		        entity.getRenegoAppoRate(),
		        entity.getLostIntroRate(),
		        entity.getLostHearingRate(),
		        entity.getLostProposalRate(),
		        entity.getInvalidRate()
		    );
		}
}
