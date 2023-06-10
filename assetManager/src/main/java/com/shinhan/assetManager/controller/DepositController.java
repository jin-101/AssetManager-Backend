package com.shinhan.assetManager.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.deposit.DepositDtoForReact;

@Controller
@RequestMapping("/deposit")
public class DepositController {
		//예적금 추가 버튼 클릭시 => 
		@PostMapping(value = "/add.do")
		@ResponseBody
		public String addDeposit(@RequestBody DepositDtoForReact[] depositList) {
			String result = "";
			for(DepositDtoForReact de : depositList) {
				System.out.println(de.toString());
				//db에 넣는 로직
			}
			result = "예/적금 추가에 성공하였습니다.";
			return result;
		}
		
}
