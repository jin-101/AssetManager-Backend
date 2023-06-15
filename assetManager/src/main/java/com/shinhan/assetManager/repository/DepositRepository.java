package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.dto.DepositDTO;
import com.shinhan.assetManager.dto.FinancialCompanyDTO;


public interface DepositRepository extends CrudRepository<DepositDTO, String>, QuerydslPredicateExecutor<DepositDTO>{
	
	//
	public DepositDTO findByFinPrdtCd(String finPrdtCd);
	
	//
	public List<DepositDTO> findByFinCo(FinancialCompanyDTO finCo);
	
	//
	//public List<DepositDTO> findByFinCoNo(String finCoNo);
}
