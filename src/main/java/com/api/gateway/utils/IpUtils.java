package com.api.gateway.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Arrays;
import java.util.List;

public final class IpUtils {
	public static List<String>  WHITE_LISTS = Arrays.asList("localhost", "httpbin.org");

	private static final String X_REAL_IP = "X-Real-IP";
	private static final String X_FORWARDED_FOR = "X-Forwarded-For";

	public static String getRealIp(ServerHttpRequest request) {
		HttpHeaders headers = request.getHeaders();

		String realIp;

		if (headers.containsKey(X_REAL_IP)) {
			realIp = String.valueOf(headers.getFirst(X_REAL_IP));
		} else if (headers.containsKey(X_FORWARDED_FOR)) {
			realIp = String.valueOf(headers.getFirst(X_FORWARDED_FOR));
		} else {
			realIp = request.getRemoteAddress() != null ?
					(request.getRemoteAddress().getAddress() != null ?
							request.getRemoteAddress().getAddress().getHostAddress() : "") : "";
		}

		return realIp;
	}
}
