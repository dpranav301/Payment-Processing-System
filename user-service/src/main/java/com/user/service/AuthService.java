package com.user.service;

import com.user.dto.request.LoginRequest;
import com.user.dto.response.LoginResponse;

public interface AuthService {

	LoginResponse login(LoginRequest request);
	LoginResponse refreshToken(String refreshToken);
}
