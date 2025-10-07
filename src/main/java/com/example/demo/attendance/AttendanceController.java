package com.example.demo.attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;


@RestController
public class AttendanceController {

	private final AttendanceService attendanceService;

	@Autowired
	public AttendanceController (AttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}
	
	
	//DBに登録し、さらにスプシに出力（スプシの方はまだサービスロジック付くってない）
	@PostMapping("/api/attendance")
	public ResponseEntity<Attendance> postAttendance(@RequestBody @Valid AttendanceRequest request) {
		Attendance attendance = attendanceService.createAttendance(
				request.getUserId(),
				request.getRecordedAt(),
				request.getRecordType(),
				request.isAgreement());
		return new ResponseEntity<>(attendance,HttpStatus.CREATED);
				
	}
	
	
}
