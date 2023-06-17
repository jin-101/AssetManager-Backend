package com.shinhan.assetManager.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.shinhan.assetManager.dto.CarInfomationDTO;


public interface CarInfomationRepository extends CrudRepository<CarInfomationDTO, String>{

	// 제조사, 모델(class)명, 연식에 맞는 차의 평균 가격을 구하는 쿼리
	@Query("SELECT floor(avg(i.price)) " +
            "FROM CarInfomationDTO i " +
            "JOIN i.carModel m " +
            "WHERE m.carCompany.companyId = ?1 " +
            "AND m.className = ?2 " +
            "AND i.year= ?3 " +
            "GROUP BY m.className")
	public Integer getAveragePrice(@Param("companyId") Integer companyId, 
			@Param("className") String className,
			@Param("year") String year);
	
}
