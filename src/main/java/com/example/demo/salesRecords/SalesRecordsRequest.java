package com.example.demo.salesRecords;

import java.time.LocalDateTime;

import lombok.Data;

//jsから入力値を受け取るDTO
@Data
public class SalesRecordsRequest {
	
	private Integer accountId;
    private LocalDateTime callTime;
    private String telephoneNumber;
    private Integer status;
    private Integer result;
    private LocalDateTime appoTime;
    private Integer resultDetail;
    private LocalDateTime recontactDate;
    private HandlerType handler;
    private Boolean isBenefits;
    private Boolean benefitsTargetsCheck;
	
}