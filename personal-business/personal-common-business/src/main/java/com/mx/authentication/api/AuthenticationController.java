package com.mx.authentication.api;

import com.frontend.ui.IndexPage;
import com.frontend.ui.LoginPage;
import com.frontend.ui.UiComponent;
import com.mx.authentication.service.AuthenticationService;
import com.mx.authentication.service.UserNamePasswordToken;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author FizzPu
 * @since 2024/3/26 17:47
 */
@Controller
public class AuthenticationController {
	private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
	
	private final LoginPage loginPage = new LoginPage();
	private final IndexPage indexPage = new IndexPage();
	private final AuthenticationService authenticationService;

	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public UiComponent index() {
		return indexPage;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/login/index.html")
	public UiComponent loginPage() {
		return loginPage;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/auth/login")
	public UiComponent loginPage(@RequestParam("username") String userName, @RequestParam("password") String password) {
		UserNamePasswordToken args = new UserNamePasswordToken(userName, password);
		try {
			authenticationService.login(args);
			return indexPage;
		} catch (AuthenticationException authenticationException) {
			throw new IllegalArgumentException("登录失败，请检查账号和密码");
		} catch (Exception exception) {
			log.info("Exception occurred when loging.", exception);
			throw exception;
		}
	}

}
