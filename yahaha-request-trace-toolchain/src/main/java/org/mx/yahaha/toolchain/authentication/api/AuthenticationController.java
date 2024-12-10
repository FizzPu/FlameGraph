package org.mx.yahaha.toolchain.authentication.api;

import org.apache.shiro.authc.AuthenticationException;
import org.mx.yahaha.toolchain.authentication.service.AuthenticationService;
import org.mx.yahaha.toolchain.authentication.service.WebServerAuthenticator;
import org.mx.yahaha.toolchain.ui.UiComponent;
import org.mx.yahaha.toolchain.ui.login.IndexPage;
import org.mx.yahaha.toolchain.ui.login.LoginPage;
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
	public UiComponent indexPage() {
		return indexPage;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/login/index.html")
	public UiComponent loginPage() {
		return loginPage;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/auth/login")
	public UiComponent loginPage(@RequestParam("username") String userName, @RequestParam("password") String password) {
		try {
			WebServerAuthenticator.UserNamePasswordToken args = new WebServerAuthenticator.UserNamePasswordToken(userName, password);
			authenticationService.login(args);
			return indexPage;
		} catch (AuthenticationException authenticationException) {
			throw new IllegalArgumentException("登录失败，请检查账号和密码");
		} catch (Exception exception) {
			log.info("Exception occurred when logging.", exception);
			throw exception;
		}
	}
}
