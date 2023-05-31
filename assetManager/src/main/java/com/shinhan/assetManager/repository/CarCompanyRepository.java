package com.shinhan.assetManager.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.dto.CarCompanyDTO;



public interface CarCompanyRepository 
		extends CrudRepository<CarCompanyDTO, Integer>, 
				QuerydslPredicateExecutor<CarCompanyDTO>{

	
	public CarCompanyDTO findByCompanyName(String companyName);
}
