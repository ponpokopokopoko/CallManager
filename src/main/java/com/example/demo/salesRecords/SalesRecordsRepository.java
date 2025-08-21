package com.example.demo.salesRecords;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRecordsRepository extends JpaRepository<SalesRecords,Integer>{

	Optional<SalesRecords> findByRecordId(Integer id);
	
	Optional<List<SalesRecords>> findByUserIdAndIsDeletedFalse(Integer userId);

	//カスタムメソッド
}
