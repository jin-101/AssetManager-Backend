package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.dto.DepositDTO;
import com.shinhan.assetManager.dto.FinancialCompanyDTO;
import com.shinhan.assetManager.dto.SavingsDTO;

@Repository
public interface SavingsRepository 
		extends CrudRepository<SavingsDTO, String>, 
		QuerydslPredicateExecutor<SavingsDTO>{
	//
	public SavingsDTO findByFinPrdtCd(String finPrdtCd); 
	
	//
	public List<DepositDTO> findByFinCo(FinancialCompanyDTO finCo);
}
