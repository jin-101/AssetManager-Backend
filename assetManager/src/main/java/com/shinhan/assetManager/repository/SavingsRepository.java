package com.shinhan.assetManager.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.dto.SavingsDTO;


public interface SavingsRepository 
		extends CrudRepository<SavingsDTO, String>, 
		QuerydslPredicateExecutor<SavingsDTO>{
	public SavingsDTO findByFinPrdtCd(String finPrdtCd);
}
