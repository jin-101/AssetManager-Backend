package com.shinhan.assetManager.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.deposit.DepositDtoForReact;

@Controller
@RequestMapping("/deposit")
public class DepositController {
		//예적금 추가 버튼 클릭시 => 
		@PostMapping(value = "/add.do")
		public String addDeposit(@RequestBody DepositDtoForReact[] depositList) {

			String result = null;
			for(DepositDtoForReact de : depositList) {
				System.out.println(de.toString());
				result = "성공";
			}
			
			// Insert에 성공하면 성공, 실패하면 실패를 React에 보내서 => 그에 따른 Alert 창을 보여주게끔 코드 짜야 함!!
		
			return result;
		}
		
//		@PostMapping(value = "/add", consumes = "application/json", produces = "text/plain;charset=utf-8")
//		public String addDeposit(@RequestBody List<DepositDtoForReact> depositList) {
//			
//			for(DepositDtoForReact de : depositList) {
//				System.out.println(de.toString());
//			}
//			String result = null;
//			
//			// Insert에 성공하면 성공, 실패하면 실패를 React에 보내서 => 그에 따른 Alert 창을 보여주게끔 코드 짜야 함!!
//			result = "성공";
//			return result;
//		}
}
