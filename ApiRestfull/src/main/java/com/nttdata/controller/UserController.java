package com.nttdata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.dto.RegisterResponse;
import com.nttdata.dto.UserData;
import com.nttdata.service.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.OK)
	public RegisterResponse register(@RequestBody UserData userData) {
		RegisterResponse response = new RegisterResponse();

		try {
			System.out.println(userData);
			response = userService.register(userData);
			response.setCode(HttpStatus.OK.value() + "");

		} catch (Exception e) {
			response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value() + "");
			response.setError("Error al registrar : " + e.getMessage());
		}

		return response;
	}

	@PostMapping("/update/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public RegisterResponse update(@PathVariable String userId, @RequestBody UserData userData) {
		RegisterResponse response = new RegisterResponse();

		try {
			System.out.println(userData);
			userData.setUuid(userId);
			response = userService.update(userData);
			response.setCode(HttpStatus.OK.value() + "");
		} catch (Exception e) {
			response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value() + "");
			response.setError("Error al actualizar : " + e.getMessage());
		}

		return response;
	}
	
	@PostMapping("/updateE/{email}")
	@ResponseStatus(HttpStatus.OK)
	public RegisterResponse updateByEmail(@PathVariable String email, @RequestBody UserData userData) {
		RegisterResponse response = new RegisterResponse();

		try {
			System.out.println(userData);
			userData.setEmail(email);
			response = userService.updateByEmail(userData);
			response.setCode(HttpStatus.OK.value() + "");
		} catch (Exception e) {
			response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value() + "");
			response.setError("Error al actualizar : " + e.getMessage());
		}

		return response;
	}
}
