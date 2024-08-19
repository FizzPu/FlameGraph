package com.mx.authentication.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author FizzPu
 * @since 2024/3/30 22:31
 */
public interface RequestAuthenticator {

	void addExcludePath(UrlEndPoint endPoint);

	boolean allow(HttpServletRequest request, HttpServletResponse response);

}
