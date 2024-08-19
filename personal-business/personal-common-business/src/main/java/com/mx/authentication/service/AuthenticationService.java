package com.mx.authentication.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

/**
 * @author FizzPu
 * @since 2023/12/5 11:41
 */
@Service
public class AuthenticationService {
	public void login(UserNamePasswordToken args) {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout();
		}
		AuthenticationToken authenticationToken = new UsernamePasswordToken(args.getPrincipal(), args.getCredential());
		subject.login(authenticationToken);
	}
}
