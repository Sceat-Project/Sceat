package hu.sceat.backend.app.web;

import hu.sceat.backend.business.PrincipalUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PrincipalUserArgumentResolver implements HandlerMethodArgumentResolver {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return PrincipalUser.class.equals(parameter.getParameterType());
	}
	
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal != null && parameter.getParameterType().isInstance(principal)) {
			logger.debug("Successfully converted {}", principal);
			return principal;
		} else {
			logger.debug("Failed to convert {}", principal);
			throw new IllegalStateException("Current principal (" + principal + ") has incompatible type");
		}
	}
}
