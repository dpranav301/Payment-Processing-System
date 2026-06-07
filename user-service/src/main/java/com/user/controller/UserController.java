package com.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.dto.request.RegisterUser;
import com.user.dto.response.UserResponse;
import com.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	@PostMapping("/register")
	public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid RegisterUser req){
		return ResponseEntity.ok(userService.registerUser(req));
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable String userId){
		return ResponseEntity.ok(userService.getUserById(Long.parseLong(userId)));
	}
	@GetMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<UserResponse>> getAllUser(){
		return ResponseEntity.ok(userService.getAllUser());
	}
}
