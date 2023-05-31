package com.shinhan.assetManager.repository;

import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.dto.DepositOptionDTO;



public interface DepositOptionRepository
		extends CrudRepository<DepositOptionDTO, Integer> {

}
