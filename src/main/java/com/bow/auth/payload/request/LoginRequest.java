package com.bow.auth.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoginRequest {
	@NotBlank
	private String username;
	@NotBlank
	private String password;
}
