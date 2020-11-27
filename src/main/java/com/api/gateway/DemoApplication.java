package com.api.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.security.oauth2.gateway.TokenRelayGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

@SpringBootApplication
public class DemoApplication {
	@Autowired
	private TokenRelayGatewayFilterFactory filterFactory;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
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
						
						.route("resource", r -> r.path("/resource")
								.filters(f -> f.filters(filterFactory.apply())
												.removeRequestHeader("Cookie")) // Prevents cookie being sent downstream
								.uri("http://resource:9000")) // Taking advantage of docker naming
						
						.route("hello", ps-> {
							return ps.path("/hello")
									.uri("lb://helloservice");
						})
						
						.build();
	}
}
