package com.acuity.rdso.sbe_user.security;

import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
@EnableOAuth2Client
public class OAuth2Configs {
  @Bean
  @Order(0)
  public RequestContextListener requestContextListener() {
    return new RequestContextListener();
  }

  public static final String JWT_REST_TEMPLATE = "jwtRestTemplate";

  @Bean(JWT_REST_TEMPLATE)
  public RestTemplate jwtRestTemplate() {
    final RestTemplate template = new RestTemplate();
    final List<ClientHttpRequestInterceptor> interceptors = template.getInterceptors();
    interceptors.add(new TokenInterceptor());
    template.setInterceptors(Collections.singletonList(new TokenInterceptor()));
    return template;
  }
}
