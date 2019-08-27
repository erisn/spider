package com.acuity.rdso.sbe_user.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.acuity.rdso.sbe_user.SbeUserApplication;
import com.acuity.rdso.sbe_user.entities.User;
import com.acuity.rdso.sbe_user.model.UserModel;
import com.acuity.rdso.sbe_user.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SbeUserApplication.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testGetUser() throws Exception {

		ResultActions actions = mvc
				.perform(MockMvcRequestBuilders.get("/user2").header("Origin", "*")
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		MvcResult mvcResult = actions.andReturn();
		assertEquals(200, mvcResult.getResponse().getStatus());

		User[] user = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User[].class);
		assertNotNull(user);
		assertNotNull(user[0].getUsername());
	}

	@Test
	public void testPostUser() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/user").header("Origin", "*")
				.content(new ObjectMapper().writeValueAsString(loginRequest())).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
	}

	private UserModel loginRequest() {
		UserModel user = new UserModel();
		user.setUsername("userModel");
		user.setLastName("Doe");
		user.setFirstName("John");
		return user;
	}

}
