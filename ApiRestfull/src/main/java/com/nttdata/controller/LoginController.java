package com.nttdata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.dto.UserCredentials;
import com.nttdata.dto.UserData;
import com.nttdata.service.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/user")
public class LoginController {

	@Autowired
	private UserService userService;


	@PostMapping("/login")
	public UserData login(@RequestBody UserCredentials userCredentials) {
		UserData usuario = new UserData();
		try {
			usuario = userService.authenticate(userCredentials);
			usuario.setCode(HttpStatus.OK.value() + "");

		} catch (Exception e) {
			e.printStackTrace();
			usuario.setCode(HttpStatus.FORBIDDEN.value() + "");
			usuario.setError("Error en login : " + e.getMessage());
		}
		return usuario;
	}

}
