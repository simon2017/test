package com.nttdata.dto;

import lombok.Data;

@Data
public abstract class Message {

	private String error;
	private String code;
	private String token;
}
