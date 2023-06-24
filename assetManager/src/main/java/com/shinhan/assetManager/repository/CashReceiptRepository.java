package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.dto.CashReceiptDTO;

public interface CashReceiptRepository extends CrudRepository<CashReceiptDTO, Integer>{
//	public List<CashReceiptDTO> findByMemberId(String memberId);
//	
	@Query("SELECT SUM(c.usedCash) "
			+ "FROM CashReceiptDTO c "
			+ "WHERE c.memberId = ?1 "
			+ "AND YEAR(c.usedDate) = ?2 ")
	public int sumCashReceipt(String memberId, int year); 
	
}
