package com.example.demo.userAnalysis;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AgregateRepository extends JpaRepository<Agregate,Integer>{
	
	//既に登録されているデータがあるか探す係
	Optional<Agregate> findByUserIdAndSummaryDate(Integer userId, LocalDate summaryDate);

	List<Agregate> findBySummaryDate(LocalDate summaryDate);
	
}
