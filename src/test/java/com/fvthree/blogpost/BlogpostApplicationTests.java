package com.fvthree.blogpost;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fvthree.blogpost.auth.AuthController;



@ActiveProfiles("test")
@SpringBootTest
class BlogpostApplicationTests {
	
	@Autowired
	private AuthController authController;

	@Test
	void contextLoads() {
		assertNotNull(authController);
	}

}
