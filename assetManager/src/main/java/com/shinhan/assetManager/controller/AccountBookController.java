package com.shinhan.assetManager.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shinhan.assetManager.dto.AccountbookDTO;
import com.shinhan.assetManager.dto.ExcelDTO;
import com.shinhan.assetManager.dto.HouseholdAccountsCategoryDTO;
import com.shinhan.assetManager.dto.HouseholdAccountsDTO;
import com.shinhan.assetManager.repository.HouseholdAccountsCategoryRepository;
import com.shinhan.assetManager.repository.HouseholdAccountsRepository;

@RestController
@RequestMapping("/rest/webboard")
public class AccountBookController {
	
	@Autowired
	HouseholdAccountsRepository accountRepo;
	
	@Autowired
	HouseholdAccountsCategoryRepository categoryRepo;
	
	@PostMapping(value = "/list.do", consumes = "application/json") //카드내역 불러오기
	public List<HouseholdAccountsDTO> selectAll(@RequestBody AccountbookDTO dto) {
		System.out.println(dto);
		return (List<HouseholdAccountsDTO>)accountRepo.findByMonth(dto.getYear(), dto.getMonth(), dto.getMemberId());
	}
	
	@PostMapping("/categorylist.do") //카데고리 목록 불러오기
	public List<HouseholdAccountsCategoryDTO> categorylist() {
		return (List<HouseholdAccountsCategoryDTO>) categoryRepo.findAll();
	}
	
	@PutMapping(value = "/listsave.do", consumes = "application/json") //카테고리, 메모 작성 후 저장하기
	public void listsave(@RequestBody List<HouseholdAccountsDTO> itemlist) {
		System.out.println("item 리스트!!!!!!" + itemlist);
		accountRepo.saveAll(itemlist);
	}
	
	@PostMapping("/filesave.do")
    public String handleFileUpload(@RequestPart("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return "No file uploaded.";
            }

            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String storageDirectory = "C:\\Upload\\"; // 저장할 디렉토리 경로

            // 저장할 파일 경로 생성
            String filePath = storageDirectory + originalFilename;

            // 파일 저장
            file.transferTo(new File(filePath));
            
            //파일 이름에서 아이디 추출
            int index = originalFilename.indexOf("_");
            String memberId = "";
            if(index != -1) {
            	memberId = originalFilename.substring(0, index);
            }
            
            // 파일 내 데이터 추출하여 저장
            List<String[]> data = readCSV(filePath);

            List<ExcelDTO> excelDataList = new ArrayList<>();
            String[] headers = data.get(6);
   
            String[] rowHavingAcoount = data.get(2);
            String account = rowHavingAcoount[1];
            
            for (int i = 7; i < data.size(); i++) {
                String[] row = data.get(i);
                ExcelDTO excelData = new ExcelDTO();
                excelData.set계좌번호(account);
                for (int j = 0; j < headers.length; j++) {
                    String header = headers[j];
                    String value = row[j];
                 
                    // 헤더와 필드명이 일치할 경우 필드에 값을 할당
                    switch (header) {
                    
                        case "거래일자":
                        	excelData.set거래일자(value);                       	
                        	break;
                        	
                        case "거래시간":
                        	excelData.set거래시간(value);                 	
                        	break;
                        	
                        case "출금(원)":
                        	String trimmedValue1 = value.replace(",", "").replace("\"", "");
                        	if(trimmedValue1.equals("")) {
                        		trimmedValue1 = "0";
                        	}
                        	excelData.set출금(trimmedValue1);
                        	break;
                        	
                        case "입금(원)":                 	
                        	String trimmedValue2 = value.replace(",", "").replace("\"", "");
                        	
                        	if(trimmedValue2.equals("")) {
                        		trimmedValue2 = "0";
                        	}
                       	
                        	excelData.set입금(trimmedValue2);
                        	break;
                        	
                        case "내용":
                        	excelData.set내용(value);
                        	break;
                        	
                        case "잔액(원)":
                        	String trimmedValue3 = value.replace(",", "").replace("\"", "");
                        	
                        	if(trimmedValue3.equals("")) {
                        		trimmedValue3 = "0";
                        	}
                       	
                        	excelData.set잔액(trimmedValue3);
                        	break;
                        	                    
                    }
                }
                excelDataList.add(excelData);
               
            }

            // excelDataList를 활용하여 필요한 로직을 수행하거나 데이터베이스에 저장
            for (ExcelDTO excelData : excelDataList) {
            	HouseholdAccountsDTO houseDto = new HouseholdAccountsDTO();
            	
            	houseDto.setMemberId(memberId); 
            	houseDto.setAccountNumber(excelData.get계좌번호());
            	houseDto.setExchangeDate(LocalDate.parse(excelData.get거래일자()));
            	houseDto.setExchangeTime(LocalTime.parse(excelData.get거래시간()));
            	houseDto.setWithdraw(Integer.parseInt(excelData.get출금()));
            	houseDto.setDeposit(Integer.parseInt(excelData.get입금()));
            	houseDto.setContent(excelData.get내용());
            	houseDto.setBalance(Integer.parseInt(excelData.get잔액()));   
            	
            	accountRepo.save(houseDto);
            }
            return "File uploaded: " + originalFilename;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error uploading file: " + e.getMessage();
        }
    }
	
	private List<String[]> readCSV(String filePath) throws IOException {
        List<String[]> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            String[] row = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            data.add(row);
        }
        br.close();
        return data;
    }

}
