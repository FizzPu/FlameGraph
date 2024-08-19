package com.mx.server.web;

import com.frontend.ui.UiComponent;
import com.mx.authentication.annotation.EnableCommonBusiness;
import com.mx.core.EnableCoreBusiness;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.List;

/**
 * @author FizzPu
 * @since 2023/11/10 15:42
 */
@Configuration
@EnableCoreBusiness
@EnableCommonBusiness
@EnableAutoConfiguration
public class WebBootStrapConfiguration implements WebMvcConfigurer {
	@Override
	public void addReturnValueHandlers(@NonNull List<HandlerMethodReturnValueHandler> handlers) {
		WebMvcConfigurer.super.addReturnValueHandlers(handlers);
		handlers.add(new UiComponentResponseProcessor());
	}
	
	private static class UiComponentResponseProcessor implements HandlerMethodReturnValueHandler {
		@Override
		public boolean supportsReturnType(MethodParameter returnType) {
			return UiComponent.class.isAssignableFrom(returnType.getParameterType());
		}
		
		@Override
		public void handleReturnValue(@Nullable Object returnValue, @NonNull MethodParameter returnType,
			ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest) throws Exception {
			mavContainer.setRequestHandled(true);
			if (returnValue == null) {
				return;
			}
			
			UiComponent uiComponent = (UiComponent) returnValue;
			HttpServletResponse httpServletResponse = webRequest.getNativeResponse(HttpServletResponse.class);
			
			Assert.notNull(httpServletResponse, "response requires non null value");
			httpServletResponse.setContentType("text/html;charset=UTF-8");
			
			Writer writer = httpServletResponse.getWriter();
			Assert.notNull(writer, "response writer requires non null value");
			writer.write(uiComponent.getHtml());
			writer.flush();
		}
	}
}
