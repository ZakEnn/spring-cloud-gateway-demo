package com.api.gateway;

import com.api.gateway.filters.IpFilter;
import com.api.gateway.filters.IpFilter.Config;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

@SpringBootApplication
@CommonsLog
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	@Bean(name = "userRemoteAddressResolver")
	public KeyResolver userKeyResolver() {
		return exchange ->
			 Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
	}
	
	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {

		//add filter relay token for oauth2
		//  todo: add circuit breaker on rate limit request
		// todo: add docker-compose to setup dbs and keycloak
		return builder.routes()
						.route(p -> p.path("/headers")
									 .filters(f -> f.addRequestHeader("Hello", "World")
											 .requestRateLimiter()
													 .rateLimiter( RedisRateLimiter.class,
															 rl -> rl.setBurstCapacity(1).setReplenishRate(1)
											// .requestRateLimiter(r ->
											// 	r.setRateLimiter(new RedisRateLimiter(10, 10) )
											// )
									 ).and())
									 .uri("http://httpbin.org:80"))
						
						.route(p -> p.path("/httpbin/cookies")
										.and()
										.method(HttpMethod.GET)
									.filters(f -> f.setPath("/cookies"))
									.uri("http://httpbin.org:80"))
						
			            .route(p -> p.path("/anything/**")
									.filters(f -> f.filter(new IpFilter().apply(new Config())))
			            			.uri("http://httpbin.org:80"))

						.build();
	}
  
  
}
