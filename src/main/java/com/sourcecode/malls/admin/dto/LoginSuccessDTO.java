package com.sourcecode.malls.admin.dto;

import java.util.UUID;

public class LoginSuccessDTO {
	private String token = UUID.randomUUID().toString();

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
