package com.example.demo.ranking;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.salesRecords.SalesRecordsRepository;
import com.example.demo.user.Users;
import com.example.demo.user.UsersRepository;
import com.example.demo.userAnalysis.Agregate;
import com.example.demo.userAnalysis.AgregateRepository;

@Service
public class RankingService {

	private AgregateRepository agregateRepo;
	private SalesRecordsRepository salesRecordsRepo;
	private UsersRepository usersRepo;
	
	public RankingService(AgregateRepository agregateRepo,SalesRecordsRepository salesRecordsRepo,UsersRepository usersRepo) {
		this.agregateRepo = agregateRepo;
		this.salesRecordsRepo = salesRecordsRepo;
		this.usersRepo = usersRepo; 
	}
	//全体ランキング必要なサービスロジック必要
	//各ユーザーの、期間（今日、今週、今月）ごとの集計データをまとめてさらに分析
	//何を比較するか→トグルで選択可能(metric)
	
	
	//２，全体データの取得整理、表示出来る形にするロジック
	
	//ゴール：指定された項目について3つの期間（今日、今週、今月）すべてのランキング表示する
	//全ユーザーを3つの期間で情報取得
	//指定された項目についてソートして3つの期間ごとにDTOに送る

	// メイン駆動
	//Comparator（ソートをするインターフェース）
	private Comparator<Agregate> getComparatorByMetric(String metric) {
	    return switch (metric) {
	    	case "newCallCount" -> Comparator.comparing(Agregate::getNewCallCount).reversed();
	    	case "newAppoCount" -> Comparator.comparing(Agregate::getNewAppoCount).reversed();
	    	case "newAppoRate" -> Comparator.comparing(Agregate::getNewAppoRate).reversed();
	        
	       
	        case "renegoCallCount" -> Comparator.comparing(Agregate::getRenegoCallCount).reversed();
	        case "renegoAppoCount" -> Comparator.comparing(Agregate::getRenegoAppoCount).reversed();
	        case "renegoAppoRate" -> Comparator.comparing(Agregate::getRenegoAppoRate).reversed();
	        
	        case "totalCallCount" -> Comparator.comparing(Agregate::getTotalCallCount).reversed();
	        case "totalAppoCount" -> Comparator.comparing(Agregate::getTotalAppoCount).reversed();
	        case "totalAppoRate" -> Comparator.comparing(Agregate::getTotalAppoRate).reversed();
	  
	        default -> throw new IllegalArgumentException("無効な指標名: " + metric); 
	    };
	}
	
	//指定したメトリクスで並び替えしDTO（ユーザー名入り）に変換して3つの期間分まとめて返してる！
	public List<List<RankingResponseDto>> getRankingsByMetric(String metric) {
	    Comparator<Agregate> comparator = getComparatorByMetric(metric);

	    LocalDate today = LocalDate.now();
	    LocalDate weekStart = today.with(DayOfWeek.MONDAY);
	    LocalDate monthStart = today.withDayOfMonth(1);

	    return List.of(
	        convertToDtoList(agregateRepo.findBySummaryDate(today), comparator),//Agrigateを
	        convertToDtoList(agregateRepo.findBySummaryDate(weekStart), comparator),
	        convertToDtoList(agregateRepo.findBySummaryDate(monthStart), comparator)
	    );
	}

	//List<Agregate>をDTOのリストに変換
	private List<RankingResponseDto> convertToDtoList(List<Agregate> aggregates, Comparator<Agregate> comparator) {
	    return aggregates.stream()
	        .sorted(comparator)
	        .map(a -> {
	            RankingResponseDto dto = new RankingResponseDto();
	            String userName = usersRepo.findById(a.getUserId())
	                .map(Users::getUserName) // getNameは適宜読み替えて
	                .orElse("不明なユーザー");
	            dto.setUserName(userName);
	            dto.setNewCallCount(a.getNewCallCount());
	            dto.setNewAppoCount(a.getNewAppoCount());
	            dto.setNewAppoRate(a.getNewAppoRate());
	            // 他の項目も必要に応じてセット
	            return dto;
	        })
	        .collect(Collectors.toList());
	}



}
