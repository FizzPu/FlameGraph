package com.mx.authentication.annotation;

import com.mx.authentication.config.AuthenticationConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author FizzPu
 * @since 2024/3/26 17:13
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AuthenticationConfiguration.class)
public @interface EnableCommonBusiness {
}
