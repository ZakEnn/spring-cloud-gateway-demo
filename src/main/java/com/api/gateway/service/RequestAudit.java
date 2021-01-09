package com.api.gateway.service;

import com.api.gateway.dto.RequestAuditDto;
import com.api.gateway.utils.IpUtils;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class RequestAudit {

	 public Mono<RequestAuditDto> createAuditLog(ServerWebExchange exchange, RequestAuditDto entity) {
	        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
	        ServerHttpRequest request = exchange.getRequest();
	        ServerHttpResponse response = exchange.getResponse();
	        String serviceId = Objects.requireNonNull(route).getId();

	        entity.setServiceId(serviceId);
	        entity.setSourceIp(IpUtils.getRealIp(request));
	        entity.setRequestUri(request.getURI().toString());
	        if (request.getMethod() != null) {
	            entity.setRequestMethod(request.getMethod().name());
	        }
	        entity.setRequestHeaders(request.getHeaders());
	        entity.setResponseHeaders(response.getHeaders());
	        entity.setStatusCode(null == response.getStatusCode() ? HttpStatus.FORBIDDEN.value() : response.getStatusCode().value());

	        return Mono.just(entity);
	    }
}
