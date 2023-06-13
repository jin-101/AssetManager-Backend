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
	@RequestMapping(value = "/checkEmail", consumes = "application/json", produces = "text/plain;charset=UTF-8")
	public String checkEmail(@RequestBody UserDTO userDto) {
		System.out.println("메일 인증 요청 들어옴!");
		String result = service.checkEmail(userDto);

		return result;
	}

	// 로그인
	@PostMapping
	@RequestMapping(value = "/login", consumes = "application/json", produces = "text/plain;charset=UTF-8")
	public String login(@RequestBody UserDTO userDto) {
		String result = service.login(userDto);

		return result;
	}

	// 로그아웃
	@PostMapping
	@RequestMapping(value = "/logout", consumes = "application/json", produces = "text/plain;charset=UTF-8")
	public String logout(@RequestBody UserDTO userDto) {
		String result = service.logout(userDto);

		return result;
	}

	// 회원가입
	@PostMapping
	@RequestMapping(value = "/signUp", consumes = "application/json", produces = "text/plain;charset=UTF-8")
	public String signIn(@RequestBody UserDTO userDto) {
		System.out.println(userDto);
		String result = service.signUp(userDto);

		return result;
	}
	
	// 아이디 중복 체크
	@GetMapping
	@RequestMapping(value = "/checkDuplicatedId/{userId}", produces = "text/plain;charset=UTF-8")
	public String checkDuplicatedId(@PathVariable String userId) {
		String result = service.checkDuplicatedId(userId); 
		
		return result;
	}

}
