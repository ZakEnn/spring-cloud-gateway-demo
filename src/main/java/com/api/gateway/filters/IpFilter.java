package com.api.gateway.filters;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import com.api.gateway.utils.IpUtils;

import reactor.core.publisher.Mono;

public class IpFilter extends AbstractGatewayFilterFactory<IpFilter.Config> {
	private static final Logger log = LoggerFactory.getLogger(IpFilter.class);

	public IpFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		//Custom white list PreFilter to Check if remote address is within our secure range of @addr.
		return (exchange, chain) -> {			
			List<String> whitelist = IpUtils.WHITE_LISTS;
	        // verify request remote address
	        String id = exchange.getRequest().getRemoteAddress().getHostName();
	        if (!whitelist.contains(id)) {
	            ServerHttpResponse response = exchange.getResponse();
	            response.setStatusCode(HttpStatus.UNAUTHORIZED);
	            return response.setComplete();
	        }
	      
	        return chain.filter(exchange)
	        	// if you want to perform a custom postFilter         
		        .then(Mono.fromRunnable(() -> {
		        	log.info("Some post filter");
				}));
		};
	}
	public static class Config {
        // Put the configuration properties
    }

}