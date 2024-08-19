package com.mx.authentication.service;

/**
 * @author FizzPu
 * @since 2024/3/27 20:11
 */
public class UserNamePasswordToken {
	private final String principal;
	private final String credential;

	public UserNamePasswordToken(String principal, String credential) {
		this.principal = principal;
		this.credential = credential;
	}

	public String getPrincipal() {
		return principal;
	}

	public String getCredential() {
		return credential;
	}
}
