/**
 * 
 */
package com.nttdata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.controller.LoginController;
import com.nttdata.controller.UserController;
import com.nttdata.dto.RegisterResponse;
import com.nttdata.dto.UserCredentials;
import com.nttdata.dto.UserData;
import com.nttdata.utils.TestUtils;

/**
 * 
 */
@AutoConfigureMockMvc
@SpringBootTest
public class LoginControllerTests {
	@Autowired
	LoginController controller;

	@Autowired
	UserController userController;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("test.server.domain")
	private String testServer;

	@Value("test.server.port")
	private String testPort;

	@BeforeEach
	void setup() {

	}

	@Test
	void loginUser_assertOK() {
		// Primero creamos un usuario dummy
		UserData user = TestUtils.getDummyUser("loginTest");
		// lo registramos en el sistema
		RegisterResponse register = userController.register(user);
		// validamos este creado correctamente
		assertThat(register.getCode()).isEqualTo(HttpStatus.OK.value());
		// ahora intentamos realizar login
		UserCredentials credential = new UserCredentials(user.getEmail(), user.getPassword());
		try {
			URI uri = URI.create(String.format("http://%s:%s/user/login", testServer, testPort));

			MvcResult result = mockMvc
					.perform(post(uri)
							.accept(MediaType.APPLICATION_JSON)
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(objectMapper.writer().writeValueAsString(credential)))
					.andExpect(status().isOk()).andReturn();

			// recuperamos la respuesta
			UserData response = objectMapper.readValue(result.getResponse().getContentAsString(), UserData.class);

			// y validamos que el dato se haya actualizado
			assertThat(response.getName()).isEqualTo(user.getName());
			assertThat(response.getEmail()).isEqualTo(user.getEmail());
			assertThat(response.getPassword()).isEqualTo(user.getPassword());

		} catch (Exception e) {
			// si generó fallo en el request fallamos el test
			fail("Exception " + e.toString());
		}
	}

}
