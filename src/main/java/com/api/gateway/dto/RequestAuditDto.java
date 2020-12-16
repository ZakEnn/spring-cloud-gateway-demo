package com.api.gateway.dto;

import org.springframework.http.HttpHeaders;

import lombok.Data;

@Data
public class RequestAuditDto {
     String serviceId;
	 long  startTimestamp;
     String  sourceIp;
     String requestUri;
     String requestMethod;
     HttpHeaders requestHeaders;
     HttpHeaders  responseHeaders;
     int  statusCode;
     long  endTimestamp;
}
