package com.shinhan.assetManager.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.dto.HouseholdAccountsDTO;

public interface HouseholdAccountsRepository extends CrudRepository<HouseholdAccountsDTO, Integer>{
	
	//public List<HouseholdAccountsDTO> findAllByOrderByExchangeDateDesc();
	
	 //List<HouseholdAccountsDTO> findAllByOrderByExchangeDateDesc();
	
	@Query("select b"
			+ " from HouseholdAccountsDTO b"
			+ " where year(b.exchangeDate) = ?1"
			+ " and month(b.exchangeDate) = ?2"
			+ " and b.memberId = ?3"
			+ " order by b.exchangeDate desc, b.exchangeTime desc")
	public List<HouseholdAccountsDTO> findByMonth(int year, int month, String memberId);
}
