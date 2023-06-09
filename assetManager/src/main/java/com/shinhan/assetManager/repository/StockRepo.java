package com.shinhan.assetManager.repository;

import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.mapping.StockTableEntityMapping;
import com.shinhan.assetManager.stock.StockTableEntity;

public interface StockRepo extends CrudRepository<StockTableEntity, String>{
	public StockTableEntityMapping findByCorpname(String corpname);
}


