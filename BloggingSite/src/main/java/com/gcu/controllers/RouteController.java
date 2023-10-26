package com.gcu.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gcu.models.BlogForm;
import com.gcu.models.Blogger;
import com.gcu.models.LoginForm;
import com.gcu.models.RegisterForm;
import com.gcu.services.DatabaseService;

@Controller
@RequestMapping("/")
public class RouteController 
{
	private Logger logger = LoggerFactory.getLogger(RouteController.class);
	
	private DatabaseService database;
	
	private String validUsername = "shazeb";
	private String validPassword = "root";
	
	private String AccessGranted = "null"; // state manager for login status
	
	private List<BlogForm> publishedBlogs = new ArrayList<>();
	
	// Class constructor
	public RouteController(DataSource datasource)
	{
		this.database = new DatabaseService(datasource);
		publishedBlogs.add(new BlogForm("example blog"));
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
		{
			model.addAttribute("username", validUsername);
		}
		
		logger.info("Exiting RouteController:Index() with ['AccessGranted']=" + AccessGranted);
		
		return "index.html"; 
	}
	
	/**
	 * Handle a login form submit.
	 * @param loginForm
	 * @param model
	 * @return
	 */
	@PostMapping("/")
	public String Login(@ModelAttribute LoginForm loginForm, Model model)
	{
		logger.info("Entering RouteController:Login() with ['loginForm']="+loginForm.ToString());	
		
		Blogger login_blogger_result = database.GET_LoginBlogger(loginForm); 
		
		if (login_blogger_result != null)
		{
			AccessGranted = "true";
			validUsername = login_blogger_result.getUsername();
			
			logger.info("Exiting RouteController:Login() with ['AccessGranted']="+AccessGranted + " and ['Username']="+validUsername);
			
			return Index(model);
		}
		else 
		{
			AccessGranted = "false"; 
		}
		
		logger.info("Exiting RouteController:Login() with ['AccessGranted']="+AccessGranted); 
		
		return Index(model);
	}
	
	/**
	 * Route to our homepage.
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
	 * Publish a new blog and view it on homepage.
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
	
	/**
	 * Create page for blog.
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
	 * Signup page for new blogger.
	 * @param model
	 * @return
	 */
	@GetMapping("/signup")
	public String Signup(Model model)
	{
		logger.info("Entering RouteController:Signup()");
		
		model.addAttribute("RegisterForm", new RegisterForm());
		
		logger.info("Exiting RouteController:Signup()");
		
		return "signup.html";
	}
	
	/**
	 * Handle new blogger register form.
	 * @param registerForm
	 * @param model
	 * @return
	 */
	@PostMapping("/signup")
	public String SignupSubmit(@ModelAttribute RegisterForm registerForm, Model model)
	{		
		logger.info("Entering RouteController:SignupSubmit() with RegisterForm: "+registerForm.ToString());
		
		boolean blogger_signup_result = database.POST_NewBlogger(registerForm); 
		
		logger.info("Exiting RouteController:SignupSubmit() with ['blogger_signup_result']="+blogger_signup_result);
		
		if (!blogger_signup_result)
		{
			return Index(model);
		}			
		else
		{
			AccessGranted = "true";
			validUsername = registerForm.getUsername();
			return Home(model);	
		}
				
	}
}
