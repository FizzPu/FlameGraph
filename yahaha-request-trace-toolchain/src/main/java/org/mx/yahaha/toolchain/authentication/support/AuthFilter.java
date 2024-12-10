package org.mx.yahaha.toolchain.authentication.support;

import org.mx.yahaha.toolchain.authentication.service.WebServerAuthenticator;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author FizzPu
 * @since 2024/3/30 20:44
 */
public class AuthFilter implements Filter {
	private final WebServerAuthenticator requestAuthenticator;

	public AuthFilter(WebServerAuthenticator requestAuthenticator) {
		this.requestAuthenticator = requestAuthenticator;
	}

	@Override
	public void doFilter(
			ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain
	) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		if (requestAuthenticator.allow(request, response)) {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}
}
