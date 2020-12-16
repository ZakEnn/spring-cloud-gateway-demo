package com.api.gateway.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import com.api.gateway.dto.RequestAuditDto;

import reactor.core.publisher.Mono;

@Service
public class RequestAudit {
	private static final Logger log = LoggerFactory.getLogger(RequestAudit.class);

    private static final String X_REAL_IP = "X-Real-IP";
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    
	 public Mono<RequestAuditDto> createAuditLog(ServerWebExchange exchange, RequestAuditDto entity) {        
	        log.info("Audit request data to MongoDB");
	        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
	        ServerHttpRequest request = exchange.getRequest();
	        ServerHttpResponse response = exchange.getResponse();
	        String serviceId = Objects.requireNonNull(route).getId();
	     
	        entity.setServiceId(serviceId);
	        entity.setSourceIp(getRealIp(request));
	        entity.setRequestUri(request.getURI().toString());
	        if (request.getMethod() != null) {
	            entity.setRequestMethod(request.getMethod().name());
	        }
	        entity.setRequestHeaders(request.getHeaders());
	        entity.setResponseHeaders(response.getHeaders());
	        entity.setStatusCode(null == response.getStatusCode() ? HttpStatus.FORBIDDEN.value() : response.getStatusCode().value());

	        return Mono.just(entity);
	    }

	  
	    private String getRealIp(ServerHttpRequest request) {
	        HttpHeaders headers = request.getHeaders();

	        String realIp;

	        if (headers.containsKey(X_REAL_IP)) {
	            realIp = String.valueOf(headers.getFirst(X_REAL_IP));
	        } else if (headers.containsKey(X_FORWARDED_FOR)) {
	            realIp = String.valueOf(headers.getFirst(X_FORWARDED_FOR));
	        } else {
	            realIp = request.getRemoteAddress() != null ?
	                    (request.getRemoteAddress().getAddress() != null ?
	                            request.getRemoteAddress().getAddress().getHostAddress() : "") : "";
	        }

	        return realIp;
	    }
}
