package com.nttdata.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import com.nttdata.dto.UserException;
import com.nttdata.service.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/user")
public class UserController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.OK)
	public RegisterResponse register(@RequestBody UserData userData) {
		RegisterResponse response = new RegisterResponse();

		try {
			logger.info("/user/register -> Register : %s", userData);
			response = userService.register(userData);
			response.setCode(HttpStatus.OK.value());

		} catch (UserException e) {
			response.setCode(HttpStatus.BAD_REQUEST.value());
			response.setError("Error al registrar : " + e.getMessage());
			logger.error(e.toString());
		} catch (DataIntegrityViolationException e) {
			response.setCode(HttpStatus.BAD_REQUEST.value());
			response.setError("Error al registrar : " + e.getMessage());
			logger.error(e.toString());
		} catch (Exception e) {
			response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setError("Error interno : " + e.getMessage());
			logger.error(e.toString());
		}

		return response;
	}

	@PostMapping("/update/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public RegisterResponse update(@PathVariable String userId, @RequestBody UserData userData) {
		RegisterResponse response = new RegisterResponse();

		try {
			logger.info("/user/update/%s -> Update : %s", userId, userData);
			userData.setUuid(userId);
			response = userService.updateByUUID(userData);
			response.setCode(HttpStatus.OK.value());
		} catch (UserException e) {
			response.setCode(HttpStatus.BAD_REQUEST.value());
			response.setError("Error al registrar : " + e.getMessage());
			logger.error(e.toString());
		} catch (DataIntegrityViolationException e) {
			response.setCode(HttpStatus.BAD_REQUEST.value());
			response.setError("Duplicidad de datos : " + e.getMessage());
			logger.error(e.toString());
		} catch (Exception e) {
			response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setError("Error interno : " + e.getMessage());
			logger.error(e.toString());
		}

		return response;
	}
}
