package com.shinhan.assetManager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.apt.AptRecentTradeDTO;
import com.shinhan.assetManager.apt.MultikeyForAptRecent;

@Repository 
public interface AptRecentTradeRepo extends CrudRepository<AptRecentTradeDTO, MultikeyForAptRecent>{

}
