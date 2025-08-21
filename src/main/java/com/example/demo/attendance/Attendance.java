package com.example.demo.attendance;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "attendance")
@Data
public class Attendance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attendance_id",updatable = false)
	private Integer id;
	
	@Column(name = "user_id", nullable = false)
	private Integer userId;
	
	@Column(name = "recorded_at", nullable = false)
	private LocalDateTime recordedAt;
	
    @Enumerated(EnumType.STRING) // ← Enum型に必要！
	@Column(name = "record_type", nullable = false)
	private RecordType recordType;
	
	@Column(name = "created_at",updatable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "agreement", nullable = false)
	private boolean agreement;
	
	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}
	
}

