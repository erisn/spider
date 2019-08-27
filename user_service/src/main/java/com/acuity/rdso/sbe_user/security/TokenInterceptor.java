package com.acuity.rdso.sbe_user.security;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class TokenInterceptor implements ClientHttpRequestInterceptor {
  @Override
  public ClientHttpResponse intercept(
      final HttpRequest httpRequest, final byte[] bytes, final ClientHttpRequestExecution execution)
      throws IOException {
    final HttpHeaders headers = httpRequest.getHeaders();
    headers.add("Authorization", TokenHolder.getToken());
    return execution.execute(httpRequest, bytes);
  }
}
