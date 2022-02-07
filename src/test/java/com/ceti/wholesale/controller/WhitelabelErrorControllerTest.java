package com.ceti.wholesale.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.controller.api.WhitelabelErrorController;
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
@WebMvcTest(WhitelabelErrorController.class)
@ActiveProfiles("test")
public class WhitelabelErrorControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private VoucherUtils voucherUtils;
	@MockBean
	private FactoryRepository factoryRepository;
	@Test
	public void testWhitelabelErrorController1() throws Exception {
		mockMvc.perform(get("/error")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message", is("Page not found!")));
	}

	@Test
	public void testWhitelabelErrorController2() throws Exception {
		mockMvc.perform(get("/thisPageIsNotExisted")).andExpect(status().isNotFound());
	}
}
