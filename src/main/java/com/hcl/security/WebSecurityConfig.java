package com.hcl.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
public class WebSecurityConfig {

	@Bean
	public MapReactiveUserDetailsService userDetailsService() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		UserDetails user = new User("wmapi", "{noop}Sp1tf1re", grantedAuthorities);
  
		return new MapReactiveUserDetailsService(user);
	}

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http.authorizeExchange().anyExchange().authenticated().and().httpBasic();
		
		return http.build();
	}
}