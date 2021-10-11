package com.xuanbinh.library;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


//@SpringBootTest
class LibraryApplicationTests {

	Calculator calculator = new Calculator();
	@Test
	void contextLoads() {
		int num1 = 10;
		int num2 = 20;
		int result = calculator.add(num1, num1);

		assertThat(result).isEqualTo(20);
	}

	class Calculator {
		int add (int a , int b) {
			return a + b;
		}
	}

}
