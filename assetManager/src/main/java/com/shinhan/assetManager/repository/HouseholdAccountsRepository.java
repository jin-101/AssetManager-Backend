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
			+ " order by b.exchangeDate desc")
	public List<HouseholdAccountsDTO> findByMonth(int year, int month, String memberId);
	
	
	//새로 삽입한 건의 윗부분 내역
	@Query("SELECT ha FROM HouseholdAccountsDTO ha " +
	                  "WHERE ha.exchangeDate > (SELECT ha2.exchangeDate FROM HouseholdAccountsDTO ha2 " +
	                                          "WHERE ha2.detailCode = (SELECT MAX(ha3.detailCode) FROM HouseholdAccountsDTO ha3))")
	public List<HouseholdAccountsDTO> getFilteredAccounts();
	
	//새로 삽입한 건의 아랫 부분 잔액
	@Query("SELECT ha FROM HouseholdAccountsDTO ha " +
            "WHERE ha.exchangeDate < (SELECT ha2.exchangeDate FROM HouseholdAccountsDTO ha2 " +
            "WHERE ha2.detailCode = (SELECT MAX(ha3.detailCode) FROM HouseholdAccountsDTO ha3)) " +
			"ORDER BY ha.exchangeDate DESC")
	public List<HouseholdAccountsDTO> getLastBalance();
	
	// 총자산 얻기 中 가계부잔액 얻기
	public List<HouseholdAccountsDTO> findByMemberIdOrderByExchangeDateDesc(String memberId); // OrderBy컬럼명Desc(Asc)
	
}
