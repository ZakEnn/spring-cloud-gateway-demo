# spring-cloud-gateway-demo

Spring Boot application that uses Keycloak for the authentication process of routes defined on Spring Cloud Gateway.
As well as an example of a custom IpWhiteList PreFilter and a RequestAudit GlobalFilter.

# Keycloak Overview

Keycloak framework is an authentication and authorization server that can be used to secure your applications.

It has many features as :
- Single sign-on and single sign-out
- Social login
- User federation (LDAP, Active directory)
- Centralized management with Admin console
- Standard protocols (OpenId connect 1.0, OAuth2.0, SAML 2.0)


# SCG Overview

Spring cloud gateway is an enhanced reverse proxy built on top of Spring WebFlux providing a flexible way to route requests based on a number of criteria, as well as focuses on cross-cutting concerns such as security, resiliency, and monitoring.

The main features of SCG are :
- Routes that can process requests to downstream services.
- Predicates that can match a Route on HTTP Request (Path, Method, Header, Host, etc…​).
- Filters that can modify downstream HTTP Request and HTTP Response (Add/Remove Headers, Add/Remove Parameters, Rewrite Path, Set Path, Circuit Breaker, Rate Limiting etc…​).

# Application Setup

For running docker DBs instances (Redis, MongoDB and Keycloak) We should fire up the command:

     $ docker-compose up -d

After that run the app using maven command:

     $ mvn spring-boot:run

