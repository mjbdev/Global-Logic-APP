package com.globallogic.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class UsersApplication {

	// van 5 , a las 15:00 hs
	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}

}
