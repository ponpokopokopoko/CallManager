package com.example.demo.salesRecords;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.user.UsersService;

@Controller
public class SalesRecordsController {
	
	private final SalesRecordsService salesRecordsService;
	private final UsersService usersService;
	
	@Autowired
	public SalesRecordsController(SalesRecordsService salesRecordsService,UsersService usersService) {
		this.salesRecordsService = salesRecordsService;
		this.usersService = usersService;
	}
	
	//新規情報の作成API
	@PostMapping("/api/salesRecords")
	public ResponseEntity<SalesRecords> createSalesRecords(@RequestBody SalesRecordsRequest req,Authentication auth) {
		
		// 認証情報からユーザー名やIDを取得
	    String username = auth.getName(); // principalのユーザー名

	    Integer userId = usersService.findUserIdByUsername(username); 
		
	    //データ作成
		SalesRecords reco =  salesRecordsService.createSalesRecord(req,userId);
		return new ResponseEntity<>(reco,HttpStatus.CREATED);
		
	}
	//変更API
	@PatchMapping("/api/salesRecords/{recordId}")
	public ResponseEntity<SalesRecords> patchSalesRecords(
	        @PathVariable Integer recordId,
	        @RequestBody SalesRecordsRequest req) {
	    
	    SalesRecords reco =  salesRecordsService.patchSalesRecord(req, recordId);
	    return new ResponseEntity<>(reco, HttpStatus.OK); // ← PATCHはOKでOK！
	}
	
	//削除API(論理削除)
	@DeleteMapping("/api/salesRecords/{recordId}")
	public ResponseEntity<Void> deleteSalesRecords(@PathVariable Integer recordId) {
	    salesRecordsService.deleteSalesRecord(recordId); // 実行だけ
	    return ResponseEntity.noContent().build(); // 204　削除は普通値を返さない
	}
	
	//データ取得
	@GetMapping("/api/salesRecords")//ユーザーIDは認証情報から取得
	public ResponseEntity<List<SalesRecordsResponse>> getSalesRecords(Authentication auth) {
		
		// 認証情報からユーザー名やIDを取得
	    String username = auth.getName(); 

	    Integer userId = usersService.findUserIdByUsername(username); 
		
	    List<SalesRecordsResponse> records = salesRecordsService.getSalesRecords(userId);
	   
	    return ResponseEntity.ok(records); // 200
	}




	
	

}
