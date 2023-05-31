package com.shinhan.assetManager.repository;

import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.dto.SavingsOptionDTO;



public interface SavingsOptionRepository
		extends CrudRepository<SavingsOptionDTO, Integer> {

}
