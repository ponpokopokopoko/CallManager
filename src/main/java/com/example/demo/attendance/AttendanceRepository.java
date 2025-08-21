package com.example.demo.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer>{

	//void saveAll(Attendance newAttendance);
	
	
	//↓カスタムメソッド達
	
}