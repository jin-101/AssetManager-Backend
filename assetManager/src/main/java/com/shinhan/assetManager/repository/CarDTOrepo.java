package com.shinhan.assetManager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.car.CarDTO;

@Repository 
public interface CarDTOrepo extends CrudRepository<CarDTO, Long> {
	
	// detailCode로부터 price찾기
	public CarDTO findByDetailCode(Long detailCode);

}
