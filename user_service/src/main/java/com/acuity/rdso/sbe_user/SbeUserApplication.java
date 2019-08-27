package com.acuity.rdso.sbe_user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@RestController
@EnableResourceServer
public class SbeUserApplication {

	@Bean
	  RestTemplate restTemplate() {
	    return new RestTemplate();
	  }
	
	
	 
	public static void main(String[] args) {
		SpringApplication.run(SbeUserApplication.class, args);
	}

}
