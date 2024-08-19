package com.mx.authentication.common;

import com.google.common.base.Preconditions;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author FizzPu
 * @since 2024/3/30 22:34
 */
public class WebServerAuthenticator implements RequestAuthenticator {

	private static final String LOCATION_URL_WHEN_UN_AUTHORIZED = "/login/index.html";
	private static final RequestMethod LOCATION_URL_HTTP_METHOD_WHEN_UN_AUTHORIZED = RequestMethod.GET;

	private final Collection<UrlEndPoint> urlEndPoints = new HashSet<>();

	public WebServerAuthenticator() {
		this.addExcludePath(new UrlEndPoint(LOCATION_URL_WHEN_UN_AUTHORIZED, LOCATION_URL_HTTP_METHOD_WHEN_UN_AUTHORIZED));
	}

	@Override
	public void addExcludePath(UrlEndPoint endPoint) {
		Preconditions.checkNotNull(endPoint, "param 'endPoint' requires non null value");
		Preconditions.checkNotNull(endPoint.getUrl(), "url of endPint requires non null value");
		Preconditions.checkNotNull(endPoint.getRequestMethod(), "requestMethod properties of endPint requires non null value");

		urlEndPoints.add(endPoint);
	}

	@Override
	public boolean allow(HttpServletRequest request, HttpServletResponse response) {
		for (UrlEndPoint urlEndPoint : urlEndPoints) {
			if (urlEndPoint.match(request.getRequestURI(), request.getMethod())) {
				return true;
			}
		}

		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			return true;
		}

		response.setStatus(302);
		response.setHeader("Location", LOCATION_URL_WHEN_UN_AUTHORIZED);
		return false;
	}
}
