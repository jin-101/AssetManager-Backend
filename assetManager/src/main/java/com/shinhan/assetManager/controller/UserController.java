package com.shinhan.assetManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.UserDTO;

@Controller 
public class UserController {
	
	@Autowired
	UserRepo uRepo;
	@Autowired
	UserDTO user;
	
	

}
