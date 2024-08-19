package com.mx.core.flame.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author FizzPu
 * @since 2024/4/7 21:32
 */
@Configuration
@EnableWebMvc
@ComponentScan(value = "com.mx.core")
class CoreBusinessConfiguration {
  
}
