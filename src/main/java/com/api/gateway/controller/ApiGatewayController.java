package com.api.gateway.controller;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGatewayController {

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
						.route(p -> p.path("/httpbin/**")
									 .filters(f -> f.addRequestHeader("Hello", "World"))
									 .uri("http://httpbin.org:80"))
						
						.route(p -> p.path("/httpbin/cookies")
									.and()
									.method(HttpMethod.GET)
									.filters(f -> f.setPath("/cookies"))
									.uri("http://httpbin.org:80"))
						.build();
	}
}
