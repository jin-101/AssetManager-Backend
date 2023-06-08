package com.shinhan.assetManager.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.dto.AccountbookDTO;
import com.shinhan.assetManager.dto.ExcelDTO;
import com.shinhan.assetManager.dto.HouseholdAccountsDTO;
import com.shinhan.assetManager.repository.HouseholdAccountsRepository;

@RestController
@RequestMapping("/rest/webboard")
public class Controller {
	
	@Autowired
	HouseholdAccountsRepository accountRepo;
	
	@PostMapping(value = "/list.do", consumes = "application/json")
	public List<HouseholdAccountsDTO> selectAll(@RequestBody AccountbookDTO dto) {
		System.out.println(dto);
		return (List<HouseholdAccountsDTO>)accountRepo.findByMonth(dto.getYear(), dto.getMonth());
	}
	
//	@GetMapping("/list.do")
//	public List<HouseholdAccountsDTO> selectAll(String year, String month) {
//		System.out.println((List<HouseholdAccountsDTO>)accountRepo.findByMonth(year, month));
//		System.out.println("연도!!!!!!!!!!!! : " + year + "월!!!!!!!!!! : " + month);
//		return (List<HouseholdAccountsDTO>)accountRepo.findByMonth(year, month);
//	}
}
