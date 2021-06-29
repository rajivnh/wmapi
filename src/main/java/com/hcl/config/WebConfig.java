package com.hcl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class WebConfig {
	
	public WebConfig() {
		
	}

	@Bean
	public RouterFunction<ServerResponse> resource() {
	    return RouterFunctions.resources("/wmapi/static/**", new ClassPathResource("wmapi/static/"));
	}
}
