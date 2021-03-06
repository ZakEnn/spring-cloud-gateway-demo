package com.api.gateway.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@CommonsLog
public class GatewayController {

	  @GetMapping("/oidc/configuration")
	  public Mono<?> identityProviderConfiguration(@AuthenticationPrincipal OidcUser principal) {
		  String providerAddr = principal.getIssuer().getProtocol() + "://" + principal.getIssuer().getAuthority();
		  WebClient client = WebClient.builder().baseUrl(providerAddr).build();
		  return Mono.from(client.get().uri("/auth/realms/gateway-demo/.well-known/openid-configuration").retrieve().bodyToMono(Map.class));
	  }

	  @GetMapping("/fallback/test")
	  public Mono<Map<String,String>> fallbackTest(@AuthenticationPrincipal OidcUser principal){
	  		OidcIdToken idToken = principal.getIdToken();
	  		Map<String, String> fallbackContent = new HashMap<>();
	  		fallbackContent.put("Greeting", "Hello " + idToken.getFullName() + " \ud83d\ude0e");
	  		fallbackContent.put("Message", "Server Does not work actually \ud83d\ude1e" );
	  		fallbackContent.put("Note" , "We dont even have a cache \ud83d\ude25");
	  		return  Mono.just(fallbackContent);
	  }
}
