package com.mx.core;

import com.mx.core.flame.config.CoreBusinessConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author FizzPu
 * @since 2024/4/7 21:33
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(CoreBusinessConfiguration.class)
@interface EnableCoreBusiness {
}
