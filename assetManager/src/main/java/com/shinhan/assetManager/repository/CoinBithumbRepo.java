package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.coin.CoinBithumbDTO;

@Repository
public interface CoinBithumbRepo extends CrudRepository<CoinBithumbDTO, String> {

	// 코인 이름으로 조회
	public List<CoinBithumbDTO> findByCoinNameStartingWith(String coinName); // @Id때문에 StartingWith 추가하였음

}
