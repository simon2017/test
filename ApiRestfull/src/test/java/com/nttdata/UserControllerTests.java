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

	/**
	 * Genera usuario dummy
	 * @return
	 */
	private UserData getDummyUser(String name, String email) {
		Phone phone = new Phone();
		phone.setNumber("999999999");
		phone.setCitycode(45);
		phone.setCountrycode(56);

		UserData data = new UserData();

		data.setEmail(email);
		data.setName(name);
		data.setPassword("Sssimon12");
		data.setPhones(Arrays.asList(phone));

		return data;
	}

	@Test
	void registerUser_assertOK() {
		RegisterResponse response = controller.register(getDummyUser("test_OK","ssOK@sss.com"));
		assertThat(response.getCode()).isEqualTo(HttpStatus.OK.value() + "");
	}

	@Test
	void registerUser_assertKO() {
		UserData userA=getDummyUser("test_KO_A","ssKO@sss.com");
		UserData userB=getDummyUser("test_KO_B","ssKO@sss.com");
		userB.setName(userA.getName() + "_B");
		
		//Primero guardamos data de usuario A, se espera OK
		assertThat(controller.register(userA).getCode()).isEqualTo(HttpStatus.OK.value() + "");	
		//Luego guardamos data de usuario B, se espera KO por correo duplicado
		assertThat(controller.register(userB).getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value() + "");
	}

}
