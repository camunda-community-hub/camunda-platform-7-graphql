package org.camunda.bpm.extension.graphql;

import javax.servlet.Filter;

import org.camunda.bpm.extension.graphql.auth.ProcessEngineAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class FilterConfiguration {

	@Bean
	public FilterRegistrationBean someFilterRegistration() {

		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(GraphqlAuth());
		registration.addUrlPatterns("/*");
		registration.addInitParameter("authentication-provider", "org.camunda.bpm.extension.graphql.auth.impl.HttpBasicAuthenticationProvider");
		registration.setName("camunda-auth");
		registration.setOrder(1);
		return registration;
	}

	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}

	@Bean(name = "GraphqlAuth")
	public Filter GraphqlAuth() {
	        return new ProcessEngineAuthenticationFilter();
	    }


}
