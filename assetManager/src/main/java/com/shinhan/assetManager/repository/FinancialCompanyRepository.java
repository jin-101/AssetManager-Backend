package com.shinhan.assetManager.repository;

import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.dto.FinancialCompanyDTO;


public interface FinancialCompanyRepository 
		extends CrudRepository<FinancialCompanyDTO, String>{
}
