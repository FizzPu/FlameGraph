package com.mx.authentication.config;

import com.mx.authentication.common.AuthFilter;
import com.mx.authentication.common.RequestAuthenticator;
import com.mx.authentication.common.UrlEndPoint;
import com.mx.authentication.common.WebServerAuthenticator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.Filter;

/**
 * @author FizzPu
 * @since 2024/3/28 11:11
 */
@Configuration
public class ShiroWebConfiguration {
	private static final String ADMIN_NAME = "admin";
	private static final String ADMIN_PASSWORD = "panzer";

	private static final int COOKIE_MAX_AGE_SEC = 24 * 60 * 60;
	private static final String COOKIE_NAME = "authentication_token";
	private static final String ACTIVE_SESSIONS_CACHE_NAME = "active_sessions_cache";

	@Bean
	public Cookie cookie() {
		Cookie cookie = new SimpleCookie();
		cookie.setName(COOKIE_NAME);
		cookie.setMaxAge(COOKIE_MAX_AGE_SEC);
		return cookie;
	}

	@Bean
	public CacheManager cacheManager() {
		return new MemoryConstrainedCacheManager();
	}

	@Bean
	public SessionIdGenerator sessionIdGenerator() {
		return new JavaUuidSessionIdGenerator();
	}

	@Bean
	public SessionDAO sessionDAO(CacheManager cacheManager) {
		EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();

		sessionDAO.setActiveSessionsCacheName(ACTIVE_SESSIONS_CACHE_NAME);
		sessionDAO.setSessionIdGenerator(sessionIdGenerator());
		sessionDAO.setCacheManager(cacheManager);

		return sessionDAO;
	}

	@Bean
	public SessionManager sessionManager(Cookie cookie, SessionDAO sessionDAO) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionDAO(sessionDAO);
		// clear invalid session
		sessionManager.setSessionValidationSchedulerEnabled(true);

		sessionManager.setSessionIdCookie(cookie);
		sessionManager.setSessionIdCookieEnabled(true);

		return sessionManager;
	}

	@Bean
	public Authorizer authorizer() {
		return new ModularRealmAuthorizer();
	}

	@Bean
	public SubjectFactory subjectFactory() {
		return new DefaultWebSubjectFactory();
	}

	@Bean
	public Realm realm() {
		SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
		simpleAccountRealm.addAccount(ADMIN_NAME, ADMIN_PASSWORD);
		return simpleAccountRealm;
	}

	@Bean
	public SecurityManager securityManager(
			Realm realm, Authorizer authorizer, SessionManager sessionManager,
			SubjectFactory subjectFactory, CacheManager cacheManager) {
		DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
		defaultWebSecurityManager.setAuthorizer(authorizer);
		defaultWebSecurityManager.setRealm(realm);
		defaultWebSecurityManager.setSessionManager(sessionManager);
		defaultWebSecurityManager.setCacheManager(cacheManager);
		defaultWebSecurityManager.setSubjectFactory(subjectFactory);
		SecurityUtils.setSecurityManager(defaultWebSecurityManager);
		return defaultWebSecurityManager;
	}

	@Bean
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
		filterFactoryBean.setSecurityManager(securityManager);

		return filterFactoryBean;
	}

	//The Filter does not belong to Spring Mvc . So you need to let Spring Mvc is aware of the filter.
	@Bean
	public FilterRegistrationBean<Filter> delegatingFilterProxy(AbstractShiroFilter filter) {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(filter);
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return filterRegistrationBean;
	}

	@Bean
	public RequestAuthenticator requestAuthenticator() {
		RequestAuthenticator authenticator = new WebServerAuthenticator();
		authenticator.addExcludePath(new UrlEndPoint("/auth/login", RequestMethod.POST));
		return authenticator;
	}

	@Bean
	public FilterRegistrationBean<Filter> authFilter(RequestAuthenticator requestAuthenticator) {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		AuthFilter authFilter = new AuthFilter(requestAuthenticator);
		filterRegistrationBean.setFilter(authFilter);
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
		return filterRegistrationBean;
	}

}
