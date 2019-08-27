package com.acuity.rdso.sbe_user.controllers;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.acuity.rdso.sbe_user.SbeUserApplication;
import com.acuity.rdso.sbe_user.model.LoginForm;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SbeUserApplication.class)
@AutoConfigureMockMvc(addFilters = false)
public class TokenControllerTest {

	@Value("${auth.url}")
	private String authUrl;

	@MockBean(name = "restTemplate")
	private RestTemplate restTemplate;
	
	@Autowired
	private MockMvc mvc;

	@Test
	public void postAccessToken() throws Exception {

		mvc.perform(
		        MockMvcRequestBuilders.post("/oauth/token")
		            .header("Origin", "*")
		            .content(new ObjectMapper().writeValueAsString(loginRequest()))
		            .contentType(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk())
		        .andReturn();
	}
	
	@Test
	public void testLogin() throws Exception {
		final HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	    final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
	    map.add("grant_type", "password");
	    map.add("username", loginRequest().getUsername());
	    map.add("password", loginRequest().getPassword());
		restTemplate.postForEntity(authUrl, new HttpEntity<>(map, headers), String.class);
		ArgumentCaptor<HttpEntity<MultiValueMap<String, String>>> httpEntityArgumentCaptor = ArgumentCaptor
				.forClass(HttpEntity.class);
		Mockito.verify(restTemplate).postForEntity(Mockito.eq(authUrl),
				httpEntityArgumentCaptor.capture(), Mockito.any());

		HttpEntity<MultiValueMap<String, String>> httpEntity = httpEntityArgumentCaptor.getValue();
		MultiValueMap<String, String> body = httpEntity.getBody();

		assertThat(body, Matchers.notNullValue());
		assertThat(body, hasKey("grant_type"));
		assertThat(body, hasKey("username"));
		assertThat(body, hasKey("password"));
		assertThat(body.getFirst("username"), equalTo("businessuser"));
	    assertThat(body.getFirst("password"), equalTo("abc"));
	    assertThat(body.getFirst("grant_type"), equalTo("password"));
	}
	
	private static LoginForm loginRequest() {
		LoginForm request = new LoginForm();
		request.setUsername("businessuser");
		request.setPassword("abc");
		return request;
	}

}
