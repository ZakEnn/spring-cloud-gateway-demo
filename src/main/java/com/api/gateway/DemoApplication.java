package com.api.gateway;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		
		//add filter relay token for oauth2
		return builder.routes()
						.route(p -> p.path("/httpbin/**")
									 .filters(f -> f.addRequestHeader("Hello", "World") )
									 .uri("http://httpbin.org:80"))
						
						.route(p -> p.path("/httpbin/cookies")
									.and()
									.method(HttpMethod.GET)
									.filters(f -> f.setPath("/cookies"))
									.uri("http://httpbin.org:80"))
						
			            .route(p -> p
			            		.host("*.hystrix.com")
			            		.filters(f -> f
			            				.hystrix(config -> config
			            						.setName("mycmd")
			            						.setFallbackUri("forward:/fallback")))
			            		.uri("http://httpbin.org:80"))
						
						.build();
	}
	
  
  
}
