package com.nttdata.dto;

import lombok.Data;

@Data
public class RegisterResponse extends UserData {
	private String created;
	private String modified;
	private String last_login;
	private boolean isactive;
}
