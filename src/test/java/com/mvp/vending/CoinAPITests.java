package com.mvp.vending;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvp.vending.entity.Coin;
import com.mvp.vending.repository.CoinRepository;
import com.mvp.vending.repository.UserRepository;


public class CoinAPITests extends MvpVendingMachineApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private CoinRepository coinRepository;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testAddingNewCoin() throws Exception {
		Coin newCoin = new Coin(100, 3);
		mockMvc.perform(post("/Vendingmachine1/coins")
				.contentType("application/json")
				.param("machineId", "Vendingmachine1")
	            .content(objectMapper.writeValueAsString(newCoin)))
	            .andExpect(status().isOk());
   
		    final Coin[] foundCoin = {null}; 
	        this.coinRepository.findByMachineName("Vendingmachine1").forEach((Coin coin) -> {
	            if (coin.value == newCoin.value) {
	                foundCoin[0] = coin;
	            }
	        });
		    assertThat(foundCoin[0].getValue()).isEqualTo(100);
	
	}
	
}
