package com.nttdata;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LaunchApplicationTests {

	String hi;

	@BeforeEach
	void configuration() { 
		this.hi = "hi";
	}

	@Test
	void test() {
		assertThat(this.hi).isEqualTo("hi");
	}

}
