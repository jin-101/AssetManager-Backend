package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.coin.CoinUpbitDTO;

// 코인 Repo도 그냥 1개로 통일하는 게 맞을 듯. (어차피 기능은 같으니까) (아 근데 DTO가 1개밖에 매핑이 안되서 어쩔 수 없이 repo 여러개 생성해야 하나??)
@Repository
public interface CoinUpbitRepo extends CrudRepository<CoinUpbitDTO, String> {
	
	// 코인 이름으로 조회 
	public List<CoinUpbitDTO> findByCoinNameStartingWith(String coinName); // @Id때문에 StartingWith 추가하였음

}
