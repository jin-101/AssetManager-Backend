package com.shinhan.assetManager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.deposit.DepositSavingsDTO;

@Repository
public interface DepositDTORepo extends CrudRepository<DepositSavingsDTO, Long>{
	
	// List<detailCode>로부터 예적금 price 얻기 
	public DepositSavingsDTO findByDetailCode(Long detailCode);

}
