/**
 * 
 */
package com.nttdata.utils;

import java.util.Arrays;

import com.nttdata.dto.Phone;
import com.nttdata.dto.UserData;

/**
 * 
 */
public  class TestUtils {
	/**
	 * Genera usuario dummy
	 * 
	 * @return
	 */
	public static UserData getDummyUser() {
		return getDummyUser("test", "test@test.com", "Tttest12");
	}
	
	/**
	 * Genera usuario dummy
	 * 
	 * @return
	 */
	public static UserData getDummyUser(String name) {
		return getDummyUser(name, "test@test.com");
	}

	/**
	 * Genera usuario dummy
	 * 
	 * @return
	 */
	public static UserData getDummyUser(String name, String email) {
		return getDummyUser(name, email, "Tttest12");
	}

	/**
	 * Genera usuario dummy
	 * 
	 * @param name
	 * @param email
	 * @param password
	 * @return
	 */
	public static UserData getDummyUser(String name, String email, String password) {
		Phone phone = new Phone();
		phone.setNumber("999999999");
		phone.setCitycode(45);
		phone.setCountrycode(56);

		UserData data = new UserData();

		data.setEmail(email);
		data.setName(name);
		data.setPassword(password);
		data.setPhones(Arrays.asList(phone));

		return data;
	}
}
