package com.nttdata.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserData extends Message {
	private String uuid;
	private String name;
	private String email;
	private String password;
	private List<Phone> phones;

	public String toString() {
		return String.format("name;%s email:%s pswd:%s phones:%s ", this.getName(), this.getEmail(), this.getPassword(),
				this.getPhones());
	}
}
