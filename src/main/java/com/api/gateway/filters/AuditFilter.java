package com.api.gateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.api.gateway.dto.RequestAuditDto;
import com.api.gateway.service.RequestAudit;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuditFilter implements GlobalFilter {
  
    private Environment env;

   
    private final ReactiveMongoTemplate mongoTemplate;

    @Autowired
    RequestAudit requestAudit;
    
    @Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
         long startMillis = System.currentTimeMillis();

         if (!Boolean.valueOf(env.getProperty("audit.enabled"))) {
             return chain.filter(exchange);
         }
         
    	 RequestAuditDto requestAuditDto = new RequestAuditDto();

         return chain.filter(exchange)
                 .then()
                 .doOnSubscribe(t -> {
                	 requestAuditDto.setStartTimestamp(startMillis);
                	 requestAudit.createAuditLog(exchange, requestAuditDto)
                	 .subscribe();
                 }).doOnSuccess(aVoid  ->{
                  	requestAuditDto.setEndTimestamp(System.currentTimeMillis() - startMillis);
                  	mongoTemplate.insert(requestAudit);
                 	}
                 );
      }


   


	
}