package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.dto.DepositDTO;
import com.shinhan.assetManager.dto.DepositOptionDTO;

// 예금
@Repository
public interface DepositOptionRepository
		extends CrudRepository<DepositOptionDTO, Integer> {
	
	// save_trm이 12개월인 놈만 골라서 => 예금 금리 뽑기
	public List<DepositOptionDTO> findBySaveTrm(Integer saveTrm);
	
	// save_trm(12개월) + deposit_id 
	public DepositOptionDTO findBySaveTrmAndDeposit(Integer saveTrm, DepositDTO deposit);
	
	// deposit_id
	//public List<DepositOptionDTO> findByDeposit(DepositDTO deposit);
	
	public DepositOptionDTO findByDepositAndDepositOptionId(DepositDTO deposit, Integer depositOptionId);

	// 예금 평균금리 구하는 메소드 (땜빵용)
	public List<DepositOptionDTO> findByDeposit(DepositDTO deposit);
}
