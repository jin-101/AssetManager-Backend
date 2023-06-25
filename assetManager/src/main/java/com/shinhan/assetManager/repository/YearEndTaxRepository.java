package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.dto.YearEndTaxDTO;

public interface YearEndTaxRepository extends CrudRepository<YearEndTaxDTO, Integer>{
	public YearEndTaxDTO findByMemberId(String memberId);
	public YearEndTaxDTO findByMemberIdAndYear(String memberId, int year);
}
