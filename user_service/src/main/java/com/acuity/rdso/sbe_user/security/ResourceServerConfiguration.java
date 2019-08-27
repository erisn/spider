package com.acuity.rdso.sbe_user.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  @Override
  public void configure(final HttpSecurity http) throws Exception {

  http.authorizeRequests()
  .antMatchers(HttpMethod.OPTIONS, "/**")
  .permitAll()
  .antMatchers(HttpMethod.POST, "/oauth/token")
  .permitAll()
  .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
  .permitAll()
  .antMatchers(HttpMethod.POST, "/user")
  .permitAll()
  .antMatchers(HttpMethod.GET, "/health")
  .permitAll()
  .antMatchers(HttpMethod.GET, "/user/{username}",  "/scrapeWiki/bulkmovies", "/scrapeWiki/bulkcelebrities/{role}", "/scrapeWiki/movie/{url}/{imdb}/{movieName}" , "/scrapeWiki/actor/{name}", "/scrapeWiki/director/{name}")
  .permitAll()
  .anyRequest()
  .authenticated();

  }

}
