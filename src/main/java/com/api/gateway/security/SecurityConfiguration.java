package com.api.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfiguration {
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
	    ReactiveClientRegistrationRepository clientRegistrationRepository) {

	  // Authenticate through configured OpenID Provider
	  http.oauth2Login();

	  http.logout(logout -> logout.logoutSuccessHandler(
	    new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository)));
	
	  // Require authentication for all requests
	  http.authorizeExchange().pathMatchers("/httpbin/**").permitAll()
	  	.anyExchange().authenticated();

	  // Disable CSRF in the gateway to prevent conflicts with proxied service CSRF
	  http.csrf().disable();
	  return http.build();
	}
}
