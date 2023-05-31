package com.shinhan.assetManager.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.dto.DepositDTO;


public interface DepositRepository 
		extends CrudRepository<DepositDTO, String>, 
		QuerydslPredicateExecutor<DepositDTO>{
	public DepositDTO findByFinPrdtCd(String finPrdtCd);
}
