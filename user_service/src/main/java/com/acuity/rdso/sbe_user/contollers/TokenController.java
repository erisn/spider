package com.acuity.rdso.sbe_user.contollers;


import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.acuity.rdso.sbe_user.model.LoginForm;


@RestController
public class TokenController {

  private static final Logger Logger = LoggerFactory.getLogger(TokenController.class);

  @Value("${auth.url}")
  private String authUrl;

  @Autowired private RestTemplate restTemplate;

  @CrossOrigin(origins = "*", allowedHeaders = "*")
  @PostMapping(value = "/oauth/token")
  public ResponseEntity postAccessToken(@Valid @RequestBody LoginForm loginForm) {
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("grant_type", "password");
    map.add("username", loginForm.getUsername());
    map.add("password", loginForm.getPassword());
    Logger.info("User authentication.");
    return restTemplate.postForEntity(authUrl, new HttpEntity<>(map, headers), String.class);
  }
}
