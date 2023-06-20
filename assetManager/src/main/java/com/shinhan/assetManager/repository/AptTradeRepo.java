package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.apt.AptDTO;

@Repository
public interface AptTradeRepo extends CrudRepository<AptDTO, Long> {
	
	// 3. 시, 구, 동을 3개 전부 다 고르면 => 그에 해당하는 아파트 검색이 가능하게
	public List<AptDTO> findByAreaCodeAndDong(String gu, String dong); // gu == areaCode
	
	// 22년 1월 데이터부터 시작해야 하는데.. tradeNo이 뒤죽박죽이라 만든 메소드
	public List<AptDTO> findByTradeNoGreaterThanEqual(Long tradeNo);
	
	// 23년 1월 ~ 23년 5월
	public List<AptDTO> findByTradeNoBetween(Long startNo, Long endNo);
	
	// 3개의 조건을 줘서 => 각 아파트 거래정보 select(안 겹치게)
	public List<AptDTO> findByAptNameAndAreaCodeAndNetLeasableArea(String aptName, String areaCode, String netLeasableArea);
	
}
