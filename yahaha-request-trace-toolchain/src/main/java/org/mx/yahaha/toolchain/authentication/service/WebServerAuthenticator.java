package org.mx.yahaha.toolchain.authentication.service;

import com.google.common.base.Preconditions;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author FizzPu
 * @since 2024/3/30 22:34
 */
public class WebServerAuthenticator {
	private static final String LOCATION_URL_WHEN_UN_AUTHORIZED = "/login/index.html";
	private static final RequestMethod LOCATION_URL_HTTP_METHOD_WHEN_UN_AUTHORIZED = RequestMethod.GET;

	private final Collection<UrlEndPoint> urlEndPoints = new HashSet<>();

	public WebServerAuthenticator() {
		this.addExcludePath(new UrlEndPoint(LOCATION_URL_WHEN_UN_AUTHORIZED, LOCATION_URL_HTTP_METHOD_WHEN_UN_AUTHORIZED));
	}

	public void addExcludePath(UrlEndPoint endPoint) {
		Preconditions.checkNotNull(endPoint, "param 'endPoint' requires non null value");
		Preconditions.checkNotNull(endPoint.getUrl(), "url of endPint requires non null value");
		Preconditions.checkNotNull(endPoint.getRequestMethod(), "requestMethod properties of endPint requires non null value");

		urlEndPoints.add(endPoint);
	}

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
	
	/**
	 * @author FizzPu
	 * @since 2024/5/13 下午5:58
	 */
	public static class UrlEndPoint {
		private final String url;
		private final RequestMethod requestMethod;
	
		public UrlEndPoint(String url, RequestMethod requestMethod) {
			this.url = url;
			this.requestMethod = requestMethod;
		}
	
		public String getUrl() {
			return url;
		}
	
		public RequestMethod getRequestMethod() {
			return requestMethod;
		}
	
		public boolean match(@NonNull String url, @NonNull String requestMethod) {
			return url.equals(this.url) && requestMethod.equalsIgnoreCase(this.requestMethod.name());
		}
	}
	
	/**
	 * @author FizzPu
	 * @since 2024/3/27 20:11
	 */
	public static class UserNamePasswordToken {
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
}
