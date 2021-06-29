package com.hcl;

import java.util.Base64;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

public class Driver {

	public Driver() {

	}
	
	public static void main1(String[] args) throws Exception {
		try {
			Mono<String> res = WebClient.create("http://127.0.0.1:82").post().uri("/oauth/check_token?token=abc")
			.header("Content-Type", "application/json").accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.onStatus((HttpStatus::isError), (it -> {
				return it.bodyToMono(String.class).flatMap(error -> Mono.error(new RuntimeException(error)));
			})).bodyToMono(String.class);
			
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			//res.subscribe(System.out::println);
			System.out.println("%%" + res.block());
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			
			Thread.sleep(16000);
		} catch(Exception e) {
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& " + e.getMessage());
		}
	}
	
	public static void main4(String[] args) throws Exception {
		Mono<String> res = WebClient.create("http://127.0.0.1:822").post().uri("/oauth/check_token?token=abc")
				.header("Content-Type", "application/json").accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError, clientResponse -> {
	                if(clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)){
	                	System.out.println("**************************************");
	                    return Mono.error(new HttpClientErrorException(HttpStatus.NOT_FOUND,
	                            "Entity not found."));
	                } else {
	                	System.out.println("******************YYYYYYYYYY*******************");
	                    return Mono.error(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
	                }
	            })
				.bodyToMono(String.class);
		
		res.block();
		
		Thread.sleep(16000);
	}
	
	public static void main(String[] args) throws Exception {
		Mono<String> res = WebClient.builder().baseUrl("http://127.0.0.1:82").build().post().uri("/oauth/token?grant_type=password&username=wmclientapi&password=qSnND76Sl6muJIUsxTj2Q")
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
    		System.out.println(s);
    		
    		return Mono.empty();
    	});
		
		res.block();
	}
}

