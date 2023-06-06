package com.shinhan.assetManager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.apt.AptDTO;

@Repository
public interface AptTradeRepo extends CrudRepository<AptDTO, Long> {
	
	// 3. 동을 고르면 => 그에 해당하는 아파트 검색이 가능하게
	
}
