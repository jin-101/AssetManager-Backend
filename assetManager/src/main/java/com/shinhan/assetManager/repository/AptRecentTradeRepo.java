package com.shinhan.assetManager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.apt.AptRecentTradeDTO;
import com.shinhan.assetManager.apt.MultikeyForAptRecent;

@Repository 
public interface AptRecentTradeRepo extends CrudRepository<AptRecentTradeDTO, MultikeyForAptRecent>{
	
	// aptName, dong, netLeasableArea 3가지(DTO)를 통해 최근거래내역 tradeNo 찾기
	public AptRecentTradeDTO findByAptNameAndDongAndNetLeasableArea(String aptName, String dong, String netLeasableArea);

}
