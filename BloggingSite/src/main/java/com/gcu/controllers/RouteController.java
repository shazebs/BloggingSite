package com.gcu.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gcu.Application;
import com.gcu.models.BlogForm;
import com.gcu.models.Blogger;
import com.gcu.models.LoginForm;

@Controller
@RequestMapping("/")
public class RouteController 
{
	private Logger logger = LoggerFactory.getLogger(RouteController.class);
	
	private String validUsername = "shazeb";
	private String validPassword = "root";
	
	private String AccessGranted = "null"; // state manager for login status
	
	private List<BlogForm> publishedBlogs = new ArrayList<>();
	
	// Class constructor
	public RouteController()
	{
		publishedBlogs.add(new BlogForm("example blog"));
	}
	
	/**
	 * Create a publishable blog.
	 * @param model
	 * @return
	 */
	@GetMapping("/create")
	public String Create(Model model)
	{
		model.addAttribute("AccessGranted", AccessGranted); 
		
		if (AccessGranted.equals("true"))
		{
			model.addAttribute("username", validUsername); 
			model.addAttribute("BlogForm", new BlogForm());
		}
		
		return "create.html"; 
	}
	
	/**
	 * Our landing page endpoint.
	 * @param model
	 * @return
	 */
	@GetMapping("/")
	public String Index(Model model)
	{
		logger.trace("RouteController:Index(): Landing on Index page.");
		
		LoginForm loginForm = new LoginForm(); 
		
		model.addAttribute("LoginForm", loginForm);
		model.addAttribute("AccessGranted", AccessGranted); 
		
		if (AccessGranted.equals("true")) 
			model.addAttribute("username", validUsername);
		
		logger.info("Exiting RouteController:Index with ['AccessGranted']=" + AccessGranted);
		
		return "index.html"; 
	}
	
	/**
	 * Handle a login form submit and route to the appropriate endpoint.
	 * @param loginForm
	 * @param model
	 * @return
	 */
	@PostMapping("/")
	public String Login(@ModelAttribute LoginForm loginForm, Model model)
	{
		logger.info("Entering RouteController:Login()"); 
		logger.info("['Username']='" + loginForm.getUsername() + "', ['Password']='" + loginForm.getPassword() + "'"); 
		
		if (loginForm.getUsername().trim().equals(validUsername) && loginForm.getPassword().trim().equals(validPassword))		
		{
			AccessGranted = "true";			
			
			model.addAttribute("username", validUsername);			
			
			logger.info("Exiting RouteController:Login() with ['AccessGranted']="+AccessGranted + ", ['Username']="+validUsername); 
			
			return Home(model); 
		}
		else 
			AccessGranted = "false"; 
		
		logger.info("Exiting RouteController:Login() with ['AccessGranted']="+AccessGranted); 
		
		return Index(model);
	}
	
	/**
	 * Route to our main homepage.
	 * @param model
	 * @return
	 */
	@GetMapping("/home")
	public String Home(Model model)
	{
		logger.info("Entering RouteController:Home()"); 
		
		model.addAttribute("AccessGranted", AccessGranted);		
		if (AccessGranted.equals("true")) 
		{
			model.addAttribute("username", validUsername); 
			model.addAttribute("Blogs", publishedBlogs);
		}
		
		logger.info("Exiting RouteController:Home() with ['AccessGranted']=" + AccessGranted); 
		
		return "home.html";
	}
	
	/**
	 * 
	 * @param blogForm
	 * @param model
	 * @return
	 */
	@PostMapping("/home")
	public String PublishBlog(@ModelAttribute BlogForm blogForm, Model model)
	{
		logger.info("Entering RouteController:PublishBlog() with ['Text']="+blogForm.getText());
		
		if (AccessGranted.equals("true"))
		{
			publishedBlogs.add(blogForm); 
		}
		
		logger.info("Exiting RouteController:PublishBlog()"); 
		
		return Home(model);
	}
}
