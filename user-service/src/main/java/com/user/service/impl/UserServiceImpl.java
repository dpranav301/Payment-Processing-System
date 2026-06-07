package com.user.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.dto.request.RegisterUser;
import com.user.dto.response.UserResponse;
import com.user.entity.User;
import com.user.enums.Role;
import com.user.exception.UserAlreadyExistException;
import com.user.exception.UserNotFoundException;
import com.user.repository.UserRepository;
import com.user.service.UserService;
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserResponse registerUser(RegisterUser registerUser) {
		if(userRepo.existsByEmail(registerUser.getEmail())) {
			throw new UserAlreadyExistException("Email Already Exists");
		}
		
		//Create User
		User user=new User();
		user.setEmail(registerUser.getEmail());
		user.setName(registerUser.getName());
		user.setPassword(passwordEncoder.encode(registerUser.getPassword()));
		user.setRole(Role.ROLE_USER);
		
		//Save in Database
		user=userRepo.save(user);
		
		//Create UserReponse
		UserResponse userResp=new UserResponse();
		userResp.setId(user.getId());
		userResp.setEmail(user.getEmail());
		userResp.setName(user.getName());
		userResp.setRole(user.getRole().name());
		return userResp;
	}

	@Override
	public UserResponse getUserById(Long id) {
		User userById = userRepo.findUserById(id);
		if(userById==null) {
			throw new UserNotFoundException("User Not Found");
		}else {
			UserResponse userResp=new UserResponse();
			userResp.setEmail(userById.getEmail());
			userResp.setId(userById.getId());
			userResp.setName(userById.getName());
			userResp.setRole(userById.getRole().name());
			return userResp;
		}
		
	}

	@Override
	public List<UserResponse> getAllUser() {
		return userRepo.findAll().stream().map(user->{
			UserResponse userResp=new UserResponse();
			userResp.setEmail(user.getEmail());
			userResp.setId(user.getId());
			userResp.setName(user.getName());
			userResp.setRole(user.getRole().name());
			return userResp;
		}).collect(Collectors.toList());
		
	}

}
