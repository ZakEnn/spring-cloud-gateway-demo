package com.api.gateway.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@CommonsLog
public class GatewayController {
	  
	  @GetMapping("/test")
	  public Mono<?> test(Mono<Principal> principal) {
		  return  principal.map(p -> {
			  OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) p;
			  log.info("Token: " + token.getAuthorizedClientRegistrationId() + "|"
					  + token.getPrincipal().getAttributes() + "|" + token.getDetails() + "|"+ token.getPrincipal().getClass());
			  OidcUser oicdUser = (OidcUser)token.getPrincipal();
			 log.info("OicdUser: "+oicdUser.getClaims()+ "|" + oicdUser.getIdToken().getClaims()+"|"+oicdUser.getAttributes());
			  return oicdUser;
		  });
	  }
}
