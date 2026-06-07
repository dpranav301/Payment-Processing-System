package com.user.service.impl;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.dto.request.LoginRequest;
import com.user.dto.response.LoginResponse;
import com.user.entity.User;
import com.user.exception.UserNotFoundException;
import com.user.repository.UserRepository;
import com.user.service.AuthService;
import com.user.utils.JwtUtil;

@Service
public class LoginServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public LoginResponse login(LoginRequest request) {
//		User user = userRepo.findByEmail(request.getEmail())
//				.orElseThrow(() -> new UserNotFoundException("User With this Email Id is Not Present"));
//		boolean passwordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
//		
//		if(!passwordMatch) {
//			throw new BadCredentialsException("Invalid Credentials.");
//		}
		
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
//		User user=(User) authenticate.getPrincipal();
		 UserDetails userDetails=(UserDetails)authenticate.getPrincipal();
		String jwtToken=jwtUtil.generateToken(userDetails.getUsername());
		String refreshToken=jwtUtil.generateRefreshToken(userDetails.getUsername());
		
		LoginResponse loginResp=new LoginResponse();
		loginResp.setAccessToken(jwtToken);
		loginResp.setRefreshToken(refreshToken);
		return loginResp;
	}

	@Override
	public LoginResponse refreshToken(String refreshToken) {
		String email=jwtUtil.extractUsername(refreshToken);
		
		String newJwtToken=jwtUtil.generateToken(email);
		LoginResponse resp=new LoginResponse();
		resp.setRefreshToken(refreshToken);
		resp.setAccessToken(newJwtToken);
		return resp;
	}

}
