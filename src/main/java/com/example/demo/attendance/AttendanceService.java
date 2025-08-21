package com.example.demo.attendance;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {

	private final AttendanceRepository attendanceRepository;
	
	
	@Autowired
	public AttendanceService(AttendanceRepository attendanceRepository) {
		this.attendanceRepository = attendanceRepository;
		
	}
	
	//受け取った入力をDBに送る
	public Attendance createAttendance(Integer userId,LocalDateTime recordedAt,RecordType recordType,boolean agreement) {
		
		//バリデーションするかどうしよう？→一旦なしで
		
		 Attendance newAttendance = new Attendance();//インスタンス作成
		 
		 newAttendance.setUserId(userId);
		 newAttendance.setRecordedAt(recordedAt);
		 newAttendance.setRecordType(recordType);
		 newAttendance.setAgreement(agreement);
		
		return attendanceRepository.save(newAttendance);
		
		
	}
	
	//DBの入力をスプシに発信する

}
