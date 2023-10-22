package com.gcu.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RouteController 
{
	private Logger logger = LoggerFactory.getLogger(RouteController.class);
	
	// Class constructor
	public RouteController()
	{
		
	}
	
	@GetMapping("/")
	public String Index(Model model)
	{
		logger.trace("RouteController:Index(): Landing on Index page.");
		
		return "index.html"; 
	}
}
