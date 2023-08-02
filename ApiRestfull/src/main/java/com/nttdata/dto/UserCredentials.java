
package com.nttdata.dto;

import lombok.Data;

@Data
public class UserCredentials {
	private String email;
	private String password;

	public UserCredentials() {

	}

	/**
	 * @param email2
	 * @param password2
	 */
	public UserCredentials(String email, String password) {
		this.email = email;
		this.password = password;
	}

}
