package com.ceti.wholesale.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ceti.wholesale.controller.api.HealthCheckController;
import com.ceti.wholesale.repository.FactoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(HealthCheckController.class)
@ActiveProfiles("test")
public class HttpRequestMethodNotSupportedExceptionTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private FactoryRepository factoryRepository;

	@Test
	public void testHttpRequestMethodNotSupportedException() throws Exception {
		mockMvc.perform(post("/health/live")).andExpect(status().isMethodNotAllowed())
				.andExpect(jsonPath("$.message", is("POST /health/live is not allowed!")));
	}
}
