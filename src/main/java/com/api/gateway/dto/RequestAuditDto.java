package com.api.gateway.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;

import lombok.Data;

@Data
public class RequestAuditDto {
     String serviceId;
     LocalDateTime  requestDate;
     String  sourceIp;
     String requestUri;
     String requestMethod;
     HttpHeaders requestHeaders;
     HttpHeaders  responseHeaders;
     int  statusCode;
     long  requestDuration;
}
