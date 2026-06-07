package com.user.service;

import java.util.List;

import com.user.dto.request.RegisterUser;
import com.user.dto.response.UserResponse;

public interface UserService{

	UserResponse registerUser(RegisterUser registerUser);
	UserResponse getUserById(Long id);
	List<UserResponse> getAllUser();
}
