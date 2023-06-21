package com.shinhan.assetManager.repository;

import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.deposit.DepositSavingsDTO;

public interface DepositDTORepo extends CrudRepository<DepositSavingsDTO, Long>{

}
