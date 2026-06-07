package com.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterUser {

	@NotBlank(message = "Name Is Required")
	private String name;

	@Email(message = "Invalid Email")
	private String email;

	@NotBlank(message = "Password should be at least 6 characters")
	private String password;
}
