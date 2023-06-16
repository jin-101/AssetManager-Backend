package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.dto.DepositDTO;
import com.shinhan.assetManager.dto.DepositOptionDTO;
import com.shinhan.assetManager.dto.SavingsDTO;
import com.shinhan.assetManager.dto.SavingsOptionDTO;

@Repository
public interface SavingsOptionRepository extends CrudRepository<SavingsOptionDTO, Integer> {
	
	// 적금 평균금리 구하는 메소드 (땜빵용)
	public List<SavingsOptionDTO> findBySavings(SavingsDTO savings);
	
	// save_trm(12개월) + savings_id 
	public SavingsOptionDTO findBySaveTrmAndSavingsAndRsrvTypeNm(Integer saveTrm, SavingsDTO savings, String rsrvTypeNm);
	
	// ★ 적금은... 6개월~12개월 Between으로 사용합시다 (12개월 없는 상품이 너무 많아서 쩔 수 없다)
	public List<SavingsOptionDTO> findBySavingsAndRsrvTypeNmAndSaveTrmBetween(SavingsDTO savings, String rsrvTypeNm, Integer startTrm, Integer endTrm);

}
