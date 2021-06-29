package com.hcl.config;

import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Component
public class AddResponseHeaderWebFilter implements WebFilter {
	@Autowired
	private WebClient.Builder webClientBuilder;

	public AddResponseHeaderWebFilter() {

	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {		
		if(exchange.getRequest().getURI().getPath().equals("/wmapi/")) {
			return webClientBuilder.baseUrl("http://OAUTH").build().post().uri("/oauth/token?grant_type=password&username=wmclientapi&password=qSnND76Sl6muJIUsxTj2Q")
		    	.header("Content-Type", "application/x-www-form-urlencoded")
		    	.header("Authorization", "Basic " +  new String(Base64.getEncoder().encode("wmapi:6AFwqQ5jV527UR8n".getBytes())))
		    	.accept(MediaType.APPLICATION_JSON)
		    	.retrieve()
		    	.onStatus((HttpStatus::isError), (it -> {
					return it.bodyToMono(String.class).flatMap(error -> {
						return Mono.error(new HttpClientErrorException(it.statusCode(), new String(error.getBytes())));
					});
				}))
		    	.bodyToMono(String.class).flatMap(s -> {
		    		Map<String, String> carMap = null;
		    		
		    		try {
		    			carMap = new ObjectMapper().readValue(s.getBytes(), new TypeReference<Map<String, String>>(){});
					} catch (Exception e) {
						e.printStackTrace();
					}
		    		
		    		exchange.getResponse().getHeaders().add("X-AUTH-TOKEN", carMap.get("access_token"));
		    		
		    		return chain.filter(exchange);
		    	});
		}
		
		return chain.filter(exchange);
	}
}