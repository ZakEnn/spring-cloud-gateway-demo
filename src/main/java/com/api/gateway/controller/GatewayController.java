package com.api.gateway.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class GatewayController {
	  public static final String SECURED = "/secured";
	  public static final String UNSECURED = "/unsecured";

	  @Value("${remote.home}")
	  private URI home;

	  @GetMapping(SECURED)
	  public Mono<ResponseEntity<byte[]>> secured(
	    @AuthenticationPrincipal Jwt jwt,
	    ProxyExchange<byte[]> proxy) {
	    return proxy.uri(home.toString() + "/auth/realms/master").get();
	  }

	  @GetMapping(UNSECURED)
	  public Mono<ResponseEntity<byte[]>> unsecured(ProxyExchange<byte[]> proxy) {
	    return proxy.uri(home.toString() + "/auth/realms/master").get();
	  }
}
