package com.shinhan.assetManager.repository;

import java.util.List;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.dto.CarModelDTO;


public interface CarModelPRepository 
		extends CrudRepository<CarModelDTO, Integer>, 
			QuerydslPredicateExecutor<CarModelDTO>{

	public CarModelDTO findByClassNameAndCarNameAndModelName(String className, String carName, String modelName);
	public List<CarModelDTO> findByCarCompanyCompanyId(Integer companyId);
}
