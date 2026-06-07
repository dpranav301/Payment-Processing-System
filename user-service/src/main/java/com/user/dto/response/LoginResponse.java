package com.user.dto.response;

import lombok.Data;

@Data
public class LoginResponse {

	private String accessToken;

    private String refreshToken;
}
