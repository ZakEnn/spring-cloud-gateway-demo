package com.api.gateway.filters;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
                	 LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(startMillis), ZoneId.systemDefault());
                	 requestAuditDto.setRequestDate(date);
                	 requestAudit.createAuditLog(exchange, requestAuditDto)
                	 .subscribe();
                 }).doOnSuccess(aVoid  ->{
                	long endMillis = System.currentTimeMillis();
                  	requestAuditDto.setRequestDuration(endMillis - startMillis);
                  	mongoTemplate.insert(requestAuditDto)
                  	.subscribe();
                 	}
                 );
      }


   


	
}