package com.shinhan.assetManager.controller;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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

//	// 로그아웃
//	@PostMapping
//	@RequestMapping(value = "/logout", consumes = "application/json", produces = "text/plain;charset=UTF-8") 
//	public String logout(@RequestBody UserDTO userDto, HttpServletRequest request) {
//		String result = service.logout(userDto, request);
//
//		return result;
//	}
	
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
	
	// 회원가입 中 아이디 중복 체크
	@GetMapping
	@RequestMapping(value = "/checkDuplicatedId/{userId}", produces = "text/plain;charset=UTF-8")
	public String checkDuplicatedId(@PathVariable String userId) {
		String result = service.checkDuplicatedId(userId); 
		
		return result;
	}
	
	// 아이디 찾기
	@PostMapping
	@RequestMapping(value = "/findUserId", consumes = "application/json", produces = "application/json;charset=UTF-8")
	public List<String> findUserId(@RequestBody UserDTO userDto) {
		List<String> result = service.findUserId(userDto); // 찾은 id List 리턴

		return result;
	}
	
	// 비밀번호 찾기
	@PostMapping
	@RequestMapping(value = "/findUserPw", consumes = "application/json", produces = "text/plain;charset=UTF-8")
	public String findUserPw(@RequestBody UserDTO userDto) {
		String result = service.findUserPw(userDto); // 찾은 pw가 리턴

		return result;
	}

}
