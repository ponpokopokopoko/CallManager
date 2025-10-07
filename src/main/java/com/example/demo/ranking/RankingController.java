package com.example.demo.ranking;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//フロントにランキングデータを送るだけ
@RestController
public class RankingController {

	private final RankingService rankingService;
	
	
	public RankingController(RankingService rankingService) {
		this.rankingService = rankingService;
		
	}
	
	//クエリパラメータ→jsではこんな感じfetch(`/api/ranking?metric=${metric}`)
	@GetMapping("/api/ranking")
	public ResponseEntity<?> getRanking(@RequestParam(required = false, defaultValue = "newAppoRate") String metric) {
	    try {
	        List<List<RankingResponseDto>> ranking = rankingService.getRankingsByMetric(metric);
	        return ResponseEntity.ok(ranking); // 200 OK
	    } catch (IllegalArgumentException e) {
	        // 指標名が不正だった場合など
	        return ResponseEntity.badRequest().body("不正なmetric指定です: " + metric);
	    } catch (Exception e) {
	        // その他のエラー
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ランキングの取得中にエラーが発生しました。");
	    }
	}

}
