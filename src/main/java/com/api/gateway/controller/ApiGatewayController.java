package com.api.gateway.controller;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGatewayController {

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
						// Simple re-route from: /get to: http://httpbin.org/80
						// And adds a simple "hello:world" HTTP Header
						.route(p -> p
										.path("/get")
										.filters(f -> f.addRequestHeader("Hello", "World"))
										.uri("http://httpbin.org:80"))
						.build();
	}
}
