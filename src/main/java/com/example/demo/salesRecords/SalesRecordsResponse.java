package com.example.demo.salesRecords;

import java.time.LocalDateTime;

import lombok.Data;

//データを返すのに使うDTO
@Data
public class SalesRecordsResponse {
		
	private Integer recordId;
	private String accountName;//データid→accountName
	private LocalDateTime callTime;
	private String telephoneNumber;
	private String status;//数字ではなく文字列送る
	private String result;//数字ではなく文字列送る
	private LocalDateTime appoTime;//null許容
	private String resultDetail;//null許容　数字ではなく文字列送る
	private LocalDateTime recontactDate;//null許容　
	private HandlerType handler;//null許容　数字ではなく文字列送る
	private Boolean isBenefits;//null許容　
	private Boolean benefitsTargetsCheck;//null許容
	    
}
