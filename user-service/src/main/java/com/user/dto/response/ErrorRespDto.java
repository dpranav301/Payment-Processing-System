package com.user.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorRespDto {

	private String message;

    private LocalDateTime timestamp;
}
