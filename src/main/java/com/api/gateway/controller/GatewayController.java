package com.api.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class GatewayController {
	  
	  @RequestMapping("/fallback")
	  public Mono<String> fallback() {
	      return Mono.just("fallback");
	  }
	  
	  @RequestMapping("/test")
	  public Mono<String> test() {
	      return Mono.just("test");
	  }
}
