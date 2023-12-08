package hu.sceat.backend.app.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(x -> x
						.requestMatchers(antMatcher("/api/auth/csrf")).permitAll()
						.requestMatchers(antMatcher("/api/auth/register/consumer")).permitAll()
						.anyRequest().authenticated())
				.exceptionHandling(x -> x
						.authenticationEntryPoint((request, response, exception) -> HttpUtil
								.respondError(response, 401, "Authentication required"))
						.accessDeniedHandler((request, response, exception) -> {
							if (request.getRequestURI().equals("/api/auth/csrf")) {
								HttpUtil.respondOk(response);
							} else {
								HttpUtil.respondError(response, 403, "Access denied");
							}
						}))
				.formLogin(x -> {
					x.loginProcessingUrl("/api/auth/login");
					x.usernameParameter("email");
					x.passwordParameter("password");
					x.permitAll();
					x.successHandler((request, response, authentication) -> HttpUtil.respondOk(response));
					x.failureHandler((request, response, exception) -> HttpUtil
							.respondError(response, 401, "Invalid credentials"));
				})
				.logout(x -> {
					x.logoutUrl("/api/auth/logout");
					x.permitAll();
					x.logoutSuccessHandler((request, response, authentication) -> HttpUtil.respondOk(response));
				})
				.cors(x -> {
					CorsConfiguration config = new CorsConfiguration();
					config.addAllowedOriginPattern("*");
					config.setAllowCredentials(true);
					config.setAllowedMethods(List.of("GET", "POST"));
					config.setAllowedHeaders(List.of("Content-Type", "X-XSRF-TOKEN"));
					x.configurationSource(request -> config);
				})
				.csrf(x -> {
					if (true) { //TODO re-enable CSRF once the frontend is ready
						x.disable();
						return;
					}
					
					x.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
					
					//Spring 6 CSRF BREACH protection is causing issues, this fixes that. For more information, see:
					//https://docs.spring.io/spring-security/reference/5.8/migration/servlet/exploits.html#_i_am_using_angularjs_or_another_javascript_framework
					//CSRF BREACH is not an issue, because a unique CSRF token is only sent as a response once.
					x.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler());
				})
				.build();
	}
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new PrincipalUserArgumentResolver());
	}
}
