package com.mx.authentication.common;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author FizzPu
 * @since 2024/5/13 下午5:58
 */
public class UrlEndPoint {
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
