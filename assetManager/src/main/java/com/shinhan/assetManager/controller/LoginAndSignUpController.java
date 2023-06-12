package com.shinhan.assetManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.service.LoginAndSignUpService;
import com.shinhan.assetManager.user.UserDTO;

@RestController 
@RequestMapping("/user")
public class LoginAndSignUpController {
	
	@Autowired
	LoginAndSignUpService service;
	
	// 이메일 체크
	@GetMapping
	@RequestMapping(value = "/checkEmail", consumes = "application/json", produces="text/plain;charset=UTF-8")
	public String checkEmail(@RequestBody UserDTO userDto) {
		System.out.println("메일 인증 요청 들어옴!");
		String result = service.checkEmail(userDto);
		
		return result;
	}
	
	// 로그인
	@PostMapping
	@RequestMapping(value = "/login", consumes = "application/json", produces="text/plain;charset=UTF-8")
	public String login(@RequestBody UserDTO userDto) {
		String result = service.login(userDto);
		
		return result;
	}
	
	
	// 회원가입
	@PostMapping
	@RequestMapping(value = "/signUp", consumes = "application/json", produces="text/plain;charset=UTF-8")
	public String signIn(@RequestBody UserDTO userDto) {
		System.out.println(userDto);
		
		// 자세한 메소드 적용 순서는 나중에
		
		return "회원가입 성공";
	}
	
}
