package com.fvthree.blogpost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BlogpostApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogpostApplication.class, args);
	}
	
	@Bean
	public static PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
