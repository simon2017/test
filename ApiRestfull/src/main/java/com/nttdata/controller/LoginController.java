package com.nttdata.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.dto.UserCredentials;
import com.nttdata.dto.UserData;
import com.nttdata.dto.UserException;
import com.nttdata.service.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/user")
public class LoginController {
	private Logger logger=LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public UserData login(@RequestBody UserCredentials userCredentials) {
		UserData usuario = new UserData();
		try {
			logger.info("/user/login -> Login : %s",usuario);
			usuario = userService.authenticate(userCredentials);
			usuario.setCode(HttpStatus.OK.value());

		} catch (UserException e) {
			e.printStackTrace();
			usuario.setCode(HttpStatus.FORBIDDEN.value());
			usuario.setError("Error en login : " + e.getMessage());
			logger.error(e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			usuario.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			usuario.setError("Error interno : " + e.getMessage());
			logger.error(e.toString());
		}
		return usuario;
	}

}
