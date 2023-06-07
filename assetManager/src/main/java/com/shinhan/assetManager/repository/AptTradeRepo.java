package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.apt.AptDTO;

@Repository
public interface AptTradeRepo extends CrudRepository<AptDTO, Long> {
	
	// 3. 시, 구, 동을 3개 전부 다 고르면 => 그에 해당하는 아파트 검색이 가능하게
	public List<AptDTO> findByAreaCodeAndDong(String gu, String dong); // gu == areaCode
	
}
