package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.dto.SavingsDTO;
import com.shinhan.assetManager.dto.SavingsOptionDTO;

@Repository
public interface SavingsOptionRepository extends CrudRepository<SavingsOptionDTO, Integer> {
	
	// 적금 평균금리 구하는 메소드 (땜빵용)
	public List<SavingsOptionDTO> findBySavings(SavingsDTO savings);

}
