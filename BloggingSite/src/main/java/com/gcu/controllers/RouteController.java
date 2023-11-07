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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gcu.models.Blog;
import com.gcu.models.BlogForm;
import com.gcu.models.Blogger;
import com.gcu.models.Comment;
import com.gcu.models.CommentForm;
import com.gcu.models.LoginForm;
import com.gcu.models.RegisterForm;
import com.gcu.services.DatabaseService;

/**
 * Here is our controller for navigating across page routes on our web application.
 */
@Controller
@RequestMapping("/")
public class RouteController 
{
	/**
	 * logging object
	 */
	private Logger logger = LoggerFactory.getLogger(RouteController.class);
	
	/**
	 * database object
	 */
	private DatabaseService database;
	
	/**
	 * Username session variable.
	 */
	private String validUsername;
	
	/**
	 * Session authorization-level.
	 */
	private String AccessGranted = "null"; // state manager for login status
	
	/**
	 * List for holding Blog objects.
	 */
	private List<Blog> publishedBlogs = new ArrayList<>();
	
	/**
	 *  Class constructor.
	 * @param datasource 
	 */
	public RouteController(DataSource datasource)
	{
		this.database = new DatabaseService(datasource);
		publishedBlogs.add(new Blog(0, "admin", "example blog", "Thu. Oct. 26, 2023 at 1:10PM"));
	}
	
	/**
	 * Our landing page endpoint.
	 * @param model 
	 * @return the landing page
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
	 * @param loginForm login form submission from user
	 * @param model
	 * @return the home page or back to login
	 */
	@PostMapping("/")
	public String Login(@ModelAttribute LoginForm loginForm, Model model)
	{
		logger.info("Entering RouteController:Login() with ['loginForm']="+loginForm.ToString());	
		
		Blogger login_blogger_result = database.GET_Blogger(loginForm); 
		
		if (login_blogger_result != null)
		{
			AccessGranted = "true";
			validUsername = login_blogger_result.getUsername();
			
			logger.info("Exiting RouteController:Login() with ['AccessGranted']="+AccessGranted + " and ['Username']="+validUsername);
			
			return Home(model);
		}
		else 
		{
			AccessGranted = "false"; 
		}
		
		logger.info("Exiting RouteController:Login() with ['AccessGranted']="+AccessGranted); 
		
		return Index(model);
	}
	
	/**
	 * Get our homepage.
	 * @param model
	 * @return home page
	 */
	@GetMapping("/home")
	public String Home(Model model)
	{
		logger.info("Entering RouteController:Home()"); 
		
		model.addAttribute("AccessGranted", AccessGranted);		
		if (AccessGranted.equals("true")) 
		{
			model.addAttribute("username", validUsername);			
			publishedBlogs = database.GET_Blogs(); 
			model.addAttribute("Blogs", publishedBlogs);
			for (Blog blog : publishedBlogs)
			{
				blog.CommentCount = database.GET_CommentCount(blog.getBlogId());
			}
		}
		
		logger.info("Exiting RouteController:Home() with ['AccessGranted']=" + AccessGranted); 
		
		return "home.html";
	}
	
	/**
	 * Publish a new blog and view it on homepage.
	 * @param blogForm blog form submission from user
	 * @param model
	 * @return home page with new blog added
	 */
	@PostMapping("/home")
	public String PublishBlog(@ModelAttribute BlogForm blogForm, Model model)
	{
		logger.info("Entering RouteController:PublishBlog() with ['username']="+blogForm.getUsername());
		
		if (AccessGranted.equals("true"))
		{			
			blogForm.setUsername(validUsername);
			
			if (database.POST_Blog(blogForm))
			{				
				logger.info("Blog was saved to database!");
			}
			else 
			{
				logger.info("Blog was NOT saved to database.");
			}
		}
		
		logger.info("Exiting RouteController:PublishBlog()"); 
		
		return Home(model);
	}
	
	/**
	 * Create page for blog.
	 * @param model
	 * @return the create blog page
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
	 * Signup page for a new blogger.
	 * @param model
	 * @return the signup page
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
	 * @param registerForm a registration form submitted by the user
	 * @param model
	 * @return the home page or back to signup page
	 */
	@PostMapping("/signup")
	public String SignupSubmit(@ModelAttribute RegisterForm registerForm, Model model)
	{		
		logger.info("Entering RouteController:SignupSubmit() with RegisterForm: "+registerForm.ToString());
		
		boolean blogger_signup_result = database.POST_Blogger(registerForm); 		
		if (blogger_signup_result)
		{
			logger.info("New Blogger signed up! Welcome "+registerForm.getUsername()); 
			Blogger login_blogger_result = database.GET_Blogger(new LoginForm(registerForm.getUsername(), registerForm.getPassword1())); 
			if (login_blogger_result != null)
			{
				logger.info("Blogger "+login_blogger_result.getUsername()+" successfully logged in.");
				AccessGranted = "true";
				validUsername = login_blogger_result.getUsername();	
				return Home(model);
			}		
			logger.info("Blogger "+registerForm.getUsername()+" succeeded in signup but failed to login.");
		}
		return Signup(model);
	}
	
	/**
	 * Open a blog in read mode.
	 * @param id open this blog id in viewing page
	 * @param model
	 * @return the viewing page
	 */
	@GetMapping("/blog/{id}")
	public String ReadBlog(@PathVariable int id, Model model)
	{		
		logger.info("Entering RouteController:ReadBlog() with ['BlogId']="+id);
		
		model.addAttribute("AccessGranted", AccessGranted);
		
		if (AccessGranted.equals("true"))
		{
			model.addAttribute("username", validUsername);	
		}
		else 
		{
			model.addAttribute("username", "unknown");
		}
		
		Blog blog = database.GET_Blog(id);
		model.addAttribute("blog", blog); 
		model.addAttribute("CommentForm", new CommentForm(blog.getBlogId()));
		model.addAttribute("Comments", database.GET_Comments(id));
		
		return "readblog.html";
	}
	
	/**
	 * Post a new comment to the specified blog.
	 * @param id the blog id to post the comment to
	 * @param commentForm a comment form submission by the user
	 * @param model
	 * @return the same blog viewing page but with addition of the new comment
	 */
	@PostMapping("/blog/{id}")
	public String PostComment(@PathVariable int id, @ModelAttribute CommentForm commentForm, Model model)
	{
		logger.info("Entering DatabaseService:PostComment() with ['CommentText']="+commentForm.getCommentText()+", ['BlogId']="+commentForm.getBlogId());
		
		if (AccessGranted.equals("true"))
		{
			boolean post_comment_result = database.POST_Comment(new Comment(commentForm.getBlogId(), commentForm.getCommentText(), validUsername));
		}
		
		return ReadBlog(commentForm.getBlogId(), model); 
	}
}
