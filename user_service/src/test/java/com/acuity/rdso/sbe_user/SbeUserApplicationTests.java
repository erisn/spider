package com.acuity.rdso.sbe_user;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import com.acuity.rdso.sbe_user.model.LoginForm;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SbeUserApplicationTests {

	@Autowired
	private MockMvc mvc;

	@MockBean(name = "restTemplate")
	private RestTemplate restTemplate;

	@Test
	public void testLoginSuccess() throws Exception {

		mvc.perform(MockMvcRequestBuilders.post("/oauth/token").header("Origin", "*")
				.content(new ObjectMapper().writeValueAsString(loginRequest())).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
	}

	@Test
	public void testLoginFailure() throws Exception {

		mvc.perform(MockMvcRequestBuilders.multipart("/oauth/token").header("Origin", "*")
				.param("grant_type", "password").param("username", "user").param("password", "abcd")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	private static LoginForm loginRequest() {
		LoginForm request = new LoginForm();
		request.setUsername("businessuser");
		request.setPassword("abc");
		return request;
	}

}
