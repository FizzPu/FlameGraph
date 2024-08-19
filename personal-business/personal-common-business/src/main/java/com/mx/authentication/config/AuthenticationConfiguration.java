package com.mx.authentication.config;

import com.mx.authentication.common.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

/**
 * @author FizzPu
 * @since 2024/3/26 16:44
 */
@EnableWebMvc
@Configuration
@ComponentScan(value = "com.mx.authentication")
public class AuthenticationConfiguration implements WebMvcConfigurer {

}
