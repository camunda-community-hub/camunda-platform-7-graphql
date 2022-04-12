package org.camunda.platform7.extension.graphql;

import org.camunda.platform7.extension.graphql.auth.JWTAuthenticationFilter;
import org.camunda.platform7.extension.graphql.auth.ProcessEngineAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

	@Value("${JWT.secret}")
	private String secret;

	@Value("${JWT.issuer}")
	private String issuer;

	@Bean
	@ConditionalOnProperty(name = "auth.Filter", havingValue = "JWT")
	public FilterRegistrationBean JWTFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter();
		registration.setFilter(jwtAuthenticationFilter);
		registration.addUrlPatterns("/*");
		registration.addInitParameter("secret", secret);
		registration.addInitParameter("issuer", issuer);
		registration.setName("JWT-Auth");
		registration.setOrder(1);
		return registration;
	}

	@Bean
	@ConditionalOnProperty(name = "auth.Filter", havingValue = "BASIC")
	public FilterRegistrationBean someFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		ProcessEngineAuthenticationFilter graphqlAuth = new ProcessEngineAuthenticationFilter();
		registration.setFilter(graphqlAuth);
		registration.addUrlPatterns("/*");
		registration.addInitParameter("authentication-provider", "org.camunda.bpm.extension.graphql.auth.impl.HttpBasicAuthenticationProvider");
		registration.setName("Basic-Auth");
		registration.setOrder(1);
		return registration;
	}

}
