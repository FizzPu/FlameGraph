package com.mx.server;

import com.mx.server.web.WebServer;

/**
 * @author FizzPu
 * @since 2023/11/10 15:40
 */
public class ApplicationBootStrap {
	public static void main(String[] args) {
		WebServer webServer = new WebServer();
		webServer.start();
	}
}
