package com.example.demo.attendance;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

//リクエストを受けるDTO
@Data
public class AttendanceRequest {

	@Column(name = "user_id", nullable = false)
	private Integer userId;
	
	@Column(name = "recorded_at", nullable = false)
	private LocalDateTime recordedAt;
	
    @Enumerated(EnumType.STRING) // ← Enum型に必要！
	@Column(name = "record_type", nullable = false)
	private RecordType recordType;

	
	@Column(name = "agreement", nullable = false)
	private boolean agreement;
	
	

}
