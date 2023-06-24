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
	@Query("SELECT ha "
			+ "FROM HouseholdAccountsDTO ha " 
	        + "WHERE ha.exchangeDate > (SELECT ha2.exchangeDate "
	        						 + "FROM HouseholdAccountsDTO ha2 "
	                                 + "WHERE ha2.detailCode = (SELECT MAX(ha3.detailCode) "
	                                 						+ " FROM HouseholdAccountsDTO ha3)) " 
	        + "AND ha.memberId = ?1 "
	        + "AND ha.accountNumber = ?2 "
	        + "ORDER BY ha.exchangeDate DESC ")
	public List<HouseholdAccountsDTO> getFilteredAccounts(String memberId, String accountNumber);
	
	//새로 삽입한 건의 아랫 부분 잔액
	@Query("SELECT ha "
			+ "FROM HouseholdAccountsDTO ha " 
			+ "WHERE ha.exchangeDate < (SELECT ha2.exchangeDate "
									 + "FROM HouseholdAccountsDTO ha2 "
									 + "WHERE ha2.detailCode = (SELECT MAX(ha3.detailCode) "
															 + "FROM HouseholdAccountsDTO ha3)) "
	        + "AND ha.memberId = ?1 "
	        + "AND ha.accountNumber = ?2 "					 												 
			+ "ORDER BY ha.exchangeDate DESC ")
	public List<HouseholdAccountsDTO> getLastBalance(String memberId, String accountNumber);
	
	//삭제한 건의 윗부분 내역
	@Query("select ha "
			+ "from HouseholdAccountsDTO ha "
			+ "where exchangeDate > (select ha2.exchangeDate "
			+ "					   	  from HouseholdAccountsDTO ha2 "
			+ "					      where detailCode = ?1) "
//			+ "AND ha.memberId = ?2 "
//			+ "AND ha.accountNumber = ?3 "
			+ "order by ha.exchangeDate desc")
	public List<HouseholdAccountsDTO> getUpListWhenDelete(int detailCode);

	// 총자산 얻기 中 가계부잔액 얻기
	public List<HouseholdAccountsDTO> findByMemberIdOrderByExchangeDateDesc(String memberId); // OrderBy컬럼명Desc(Asc)
	
	//2023년의 총 지출 더하기
	@Query("select sum(ha.withdraw) "
			+ "	from HouseholdAccountsDTO ha "
			+ "	where memberId = ?1 "
			+ "	and year(ha.exchangeDate) = ?2")
	public int sumYearWithdraw(String memberId, int year);
	
	//카테고리별 지출 금액 더해주는 함수
	@Query("select sum(ha.withdraw) "
			+ "FROM HouseholdAccountsDTO ha "
			+ "where memberId = ?1 "
			+ "and category = ?2 "
			+ "and year(ha.exchangeDate) = ?3 ")
	public int sumCategoryWithdraw(String memberId ,String category, int year);
	
	
	
}
