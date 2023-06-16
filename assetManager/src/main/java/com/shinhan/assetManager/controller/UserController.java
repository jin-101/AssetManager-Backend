package com.shinhan.assetManager.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.service.UserService;
import com.shinhan.assetManager.user.UserDTO;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService service;
	
	@GetMapping(value = "/search/{token}")
	@ResponseBody
	public Map<String,String> searchUserInfomation(@PathVariable String token) throws Exception{
		Map<String,String> userSearchInfo = service.searchUserInfomation(token);
		return userSearchInfo;
	}
	
	@PostMapping
	@RequestMapping(value = "/update", consumes = "application/json", produces = "text/plain;charset=UTF-8")
	public String updateUserInfomation(@RequestBody UserDTO userDto) {
		System.out.println(userDto);
		String result = service.updateUserInfomation(userDto);

		return result;
	}
}
