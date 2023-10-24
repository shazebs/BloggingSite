package com.gcu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application 
{
	boolean userLoggedIn = false;
	
	public static void main(String[] args) 
	{
		SpringApplication.run(Application.class, args);
	}
}
