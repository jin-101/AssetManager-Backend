package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.coin.CoinBinanceDTO;

public interface CoinBinanceRepo extends CrudRepository<CoinBinanceDTO, String> {

	// 코인 이름으로 조회
	public List<CoinBinanceDTO> findByCoinNameStartingWith(String coinName); // @Id때문에 StartingWith 추가하였음

}
