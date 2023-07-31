/**
 * 
 */
package com.nttdata;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.controller.UserController;
import com.nttdata.dto.Phone;
import com.nttdata.dto.RegisterResponse;
import com.nttdata.dto.UserData;

/**
 * 
 */
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTests {
	@Autowired
	UserController controller;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setup() {

	}

	@Test
	void registerUser_assertOK() {
		
		Phone phone = new Phone();
		phone.setNumber("999999999");
		phone.setCitycode(45);
		phone.setCountrycode(56);
		
		UserData data = new UserData();

		data.setEmail("ss@sss.com");
		data.setName("test");
		data.setPassword("Sssimon12");
		data.setPhones(Arrays.asList(phone));

		RegisterResponse response = controller.register(data);
		assertThat(response.getCode()).isEqualTo(HttpStatus.OK.value()+"");
	}
	
	@Test
	void registerUser_assertKO() {
		
		Phone phone = new Phone();
		phone.setNumber("999999999");
		phone.setCitycode(45);
		phone.setCountrycode(56);
		
		UserData data = new UserData();

		data.setEmail("");
		data.setName("");
		data.setPassword("");
		data.setPhones(Arrays.asList(phone));

		RegisterResponse response = controller.register(data);
		assertThat(response.getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value() + "");
	}

}
