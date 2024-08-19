package com.mx.server.web;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

/**
 * @author FizzPu
 * @since 2023/11/10 15:40
 */
public class
WebServer {
	public void start() {
		ApplicationContext context = new SpringApplicationBuilder()
				.web(WebApplicationType.SERVLET).
				sources(WebBootStrapConfiguration.class).run();
	}
}
