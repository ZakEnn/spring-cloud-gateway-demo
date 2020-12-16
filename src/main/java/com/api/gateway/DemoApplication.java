package com.api.gateway;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;

import com.api.gateway.filters.IpFilter;
import com.api.gateway.filters.IpFilter.Config;
import com.api.gateway.utils.IpUtils;

@SpringBootApplication
public class DemoApplication {
	@Autowired
	IpFilter ipFilter;
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		
		//add filter relay token for oauth2
		return builder.routes()
						.route(p -> p.path("/headers")
									 .filters(f -> f.addRequestHeader("Hello", "World") )
									 .uri("http://httpbin.org:80"))
						
						.route(p -> p.path("/httpbin/cookies")
									.and()
									.method(HttpMethod.GET)
									.filters(f -> f.setPath("/cookies"))
									.uri("http://httpbin.org:80"))
						
			            .route(p -> p.path("/anything").filters(f -> f.filter(ipFilter.apply(new Config())))
			            		.uri("http://httpbin.org:80"))
						
						.build();
	}
	
	
  
  
}
