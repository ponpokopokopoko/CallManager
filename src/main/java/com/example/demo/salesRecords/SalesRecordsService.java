package com.example.demo.salesRecords;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.account.AccountRepository;
import com.example.demo.account.Accounts;
import com.example.demo.salesRecords.result.ResultMaster;
import com.example.demo.salesRecords.result.ResultMasterRepository;
import com.example.demo.salesRecords.resultDetail.ResultDetailMaster;
import com.example.demo.salesRecords.resultDetail.ResultDetailMasterRepository;
import com.example.demo.salesRecords.status.StatusMaster;
import com.example.demo.salesRecords.status.StatusMasterRepository;

import jakarta.transaction.Transactional;

@Service
public class SalesRecordsService {

	private final SalesRecordsRepository salesRecordsRepo;
	private final AccountRepository accountRepo;
	private final StatusMasterRepository statusRepo;
	private final ResultMasterRepository resultRepo;
	private final ResultDetailMasterRepository resultDetailRepo;
	
	@Autowired
	public SalesRecordsService(
			SalesRecordsRepository salesRecordsRepo,
			AccountRepository accountRepo,
			StatusMasterRepository statusRepo,
			ResultMasterRepository resultRepo,
			ResultDetailMasterRepository resultDetailRepo) {
		
		
		this.salesRecordsRepo = salesRecordsRepo;
		this.accountRepo = accountRepo;
		this.statusRepo = statusRepo;
		this.resultRepo = resultRepo;
		this.resultDetailRepo = resultDetailRepo;
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	//特定のidの行を取得るロジック
	public Optional<SalesRecords> findByID(Integer id) {
		return salesRecordsRepo.findById(id);
	}
	
	//新規作成ロジック
	@Transactional
    public SalesRecords createSalesRecord(SalesRecordsRequest request,Integer userId) {
        SalesRecords record = new SalesRecords();

        record.setUserId(userId);
        record.setAccountId(request.getAccountId());
        record.setCallTime(request.getCallTime());
        record.setTelephone_number(request.getTelephoneNumber());
        record.setStatus(request.getStatus()); // ← typoに注意！後述
        record.setResult(request.getResult());
        record.setAppoTime(request.getAppoTime());
        record.setResultDetail(request.getResultDetail());
        record.setRecontactdate(request.getRecontactDate());
        record.setHandler(request.getHandler());
        record.setBenefits(request.getIsBenefits()); // boolean型は "is" getter
        record.setBenefitsTargetsCheck(request.getBenefitsTargetsCheck());

        // createdAt は @PrePersist で自動設定されるので不要！

        return salesRecordsRepo.save(record);
    }
	
	//データ編集ロジック
	@Transactional
	public SalesRecords patchSalesRecord(SalesRecordsRequest request, Integer id) {
	    SalesRecords record = salesRecordsRepo.findByRecordId(id)
	    		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "レコードが見つかりません"));	    

	    if (request.getAccountId() != null) record.setAccountId(request.getAccountId());
	    if (request.getCallTime() != null) record.setCallTime(request.getCallTime());
	    if (request.getTelephoneNumber() != null) record.setTelephone_number(request.getTelephoneNumber());
	    if (request.getStatus() != null) record.setStatus(request.getStatus());
	    if (request.getResult() != null) record.setResult(request.getResult());
	    if (request.getAppoTime() != null) record.setAppoTime(request.getAppoTime());
	    if (request.getResultDetail() != null) record.setResultDetail(request.getResultDetail());
	    if (request.getRecontactDate() != null) record.setRecontactdate(request.getRecontactDate());
	    if (request.getHandler() != null) record.setHandler(request.getHandler());
	    if (request.getIsBenefits() != null) record.setBenefits(request.getIsBenefits());
	    if (request.getBenefitsTargetsCheck() != null) record.setBenefitsTargetsCheck(request.getBenefitsTargetsCheck());

	    return salesRecordsRepo.save(record); 
	}

	
	// 論理削除ロジック
	public SalesRecords deleteSalesRecord(Integer id) {
	    SalesRecords record = salesRecordsRepo.findByRecordId(id)
	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "レコードが見つかりません"));

	    if (record.isDeleted()) {
	        return record; // すでに削除済みなら何もしない
	    }

	    record.setDeleted(true);
	    return salesRecordsRepo.save(record);
	}
	
	
	
	//特定ユーザーの記録の取得
	public List<SalesRecordsResponse> getSalesRecords(Integer userId) {
		
	    // userIdに紐づくSalesRecordsのリストを取得（なければ空リストが返る）
	    List<SalesRecords> records = salesRecordsRepo.findByUserIdAndIsDeletedFalse(userId).orElse(Collections.emptyList());

	    // Entity → ResponseDTO に変換
	    List<SalesRecordsResponse> responseList = records.stream()
	        .map(record -> {
	            SalesRecordsResponse dto = new SalesRecordsResponse();
	            dto.setRecordId(record.getRecordId());

	            // アカウントID → アカウント名に変換する処理（必要に応じて）
	            String accountName = accountRepo.findById(record.getAccountId())
	                                  .map(Accounts::getAccountName)
	                                  .orElse("不明なアカウント");
	            dto.setAccountName(accountName);
	            
	            
	            // status（ステータス）名を取得
	            String statusName = statusRepo.findById(record.getStatus())
	                .map(StatusMaster::getDisplayName)
	                .orElse(null);
	            dto.setStatus(statusName);

	            // result（結果）名を取得
	            String resultName = resultRepo.findById(record.getResult())
	                .map(ResultMaster::getDisplayName)
	                .orElse(null);
	            dto.setResult(resultName);

	            // resultDetail（詳細）名を取得（null許容）
	            String resultDetailName = Optional.ofNullable(record.getResultDetail())
	                .flatMap(id -> resultDetailRepo.findById(id))
	                .map(ResultDetailMaster::getDisplayName)
	                .orElse(null);
	            dto.setResultDetail(resultDetailName);

	            dto.setCallTime(record.getCallTime());
	            dto.setTelephoneNumber(record.getTelephone_number());
	            dto.setAppoTime(record.getAppoTime());
	            dto.setRecontactDate(record.getRecontactdate());
	            dto.setHandler(record.getHandler());
	            dto.setIsBenefits(record.getBenefits());
	            dto.setBenefitsTargetsCheck(record.getBenefitsTargetsCheck());

	            return dto;
	        })
	        .collect(Collectors.toList());

	    return responseList;
	}


	

		

}
