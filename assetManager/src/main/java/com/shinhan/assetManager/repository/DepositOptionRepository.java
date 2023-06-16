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
	
	// ★ 6~12개월 사이의 평균 예금금리를 구하기 위한 메소드
	public List<DepositOptionDTO> findByDepositAndSaveTrmBetween(DepositDTO deposit, Integer startTrm, Integer endTrm);
	
	// 12개월의 평균 예금금리를 구하기 위한 메소드
	public DepositOptionDTO findBySaveTrmAndDeposit(Integer saveTrm, DepositDTO deposit);
	
	// 예금 평균금리 구하는 메소드 (땜빵용)
	public List<DepositOptionDTO> findByDeposit(DepositDTO deposit);
	
	// save_trm이 12개월인 놈만 골라서 => 예금 금리 뽑기
	//public List<DepositOptionDTO> findBySaveTrm(Integer saveTrm);
}
