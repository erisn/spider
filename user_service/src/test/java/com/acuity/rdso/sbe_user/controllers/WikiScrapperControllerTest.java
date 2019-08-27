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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.acuity.rdso.sbe_user.SbeUserApplication;
import com.acuity.rdso.sbe_user.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SbeUserApplication.class)
@AutoConfigureMockMvc(addFilters = false)
public class WikiScrapperControllerTest {

	@Autowired
	private MockMvc mvc;
		
	@Test
	public void testGetUser() throws Exception {

		ResultActions actions = mvc
				.perform(MockMvcRequestBuilders.get("/scrapeWiki").header("Origin", "*")
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		MvcResult mvcResult = actions.andReturn();
		assertEquals(200, mvcResult.getResponse().getStatus());

	}
}
