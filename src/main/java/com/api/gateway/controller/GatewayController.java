package com.api.gateway.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@CommonsLog
public class GatewayController {

	  @GetMapping("/oidc/configuration")
	  public Mono<?> test(Mono<Principal> principal) {
		  WebClient client = WebClient.builder().baseUrl("http://localhost:9999/").build();
		  return Mono.from(client.get().uri("/auth/realms/gateway-demo/.well-known/openid-configuration").retrieve().bodyToMono(Map.class));
	  }

	  @GetMapping("/fallback/test")
	  public Mono<Map<String,String>> fallbackTest(ServerWebExchange serverWebExchange){
		  return serverWebExchange.getPrincipal().map(p -> {
			  OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) p;
			  OidcUser oidcUser = (OidcUser) token.getPrincipal();
			  OidcIdToken idToken = oidcUser.getIdToken();

			  Map<String, String> fallbackContent = new HashMap<>();
				fallbackContent.put("Greeting", "Hello " + idToken.getFullName() + " \ud83d\ude0e");
				fallbackContent.put("Message", "Server Does not work actually \ud83d\ude1e" );
				fallbackContent.put("Note" , "We dont even have a cache \ud83d\ude25");
		 		return  fallbackContent;
		  });

	  }
}
