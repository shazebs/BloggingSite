package com.gcu.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.gcu.models.Blog;
import com.gcu.models.BlogForm;
import com.gcu.models.Blogger;
import com.gcu.models.Comment;
import com.gcu.models.LoginForm;
import com.gcu.models.RegisterForm;

/**
 * 
 * @author Shazeb Suhail
 *
 */
public class DatabaseService 
{
	@Autowired
	@SuppressWarnings("unused")
	private JdbcTemplate database; 

	private static Logger logger = LoggerFactory.getLogger(DatabaseService.class);	
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a, M-dd-yyyy");
	private LocalDateTime timestamp;
	
	/**
	 * Class Constructor. 
	 * @param dataSource
	 */
	public DatabaseService(DataSource dataSource)
	{
		this.database = new JdbcTemplate(dataSource);
	}
	
	/**
	 * Register a new Blogger in the database.
	 * @param registerForm
	 * @return
	 */
	public boolean POST_Blogger(RegisterForm registerForm)
	{
		logger.info("Entering DatabaseService:POST_NewBlogger() with RegisterForm: "+registerForm.ToString());
		
		int queryResult = -1;	
		boolean does_username_exist = GET_UsernameExists(registerForm.getUsername());		
		if (!does_username_exist && registerForm.getPassword1().equals(registerForm.getPassword2()))
		{
			try {
				String sql = "INSERT INTO bloggers (user_name, pass_word) VALUES (?, ?)";
				queryResult = database.update(sql, registerForm.getUsername(), registerForm.getPassword1());
			}
			catch (Exception e)
			{
				logger.error(e.getMessage());
				e.printStackTrace();			
				logger.info("Error adding new Blogger to database.");
			}
		}		
		
		logger.info("Exiting DatabaseService:POST_NewBlogger() with boolean: " + (queryResult > 0) );
		
		return queryResult > 0;
	}
	
	/**
	 * 
	 * @param blogForm
	 * @return
	 */
	public boolean POST_Blog(BlogForm blogForm)
	{
		timestamp = LocalDateTime.now();  
        blogForm.setTimestamp(timestamp.format(formatter));
        
		int queryResult = -1;
		try {
			String sql = "INSERT INTO blogs (user_name, blog_text, time_stamp) VALUES (?, ?, ?)";
			queryResult = database.update(sql, blogForm.getUsername(), blogForm.getBlogText(), blogForm.getTimestamp());
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		logger.info("Exiting DatabaseService:POST_Blog() with "+(queryResult > 0));
		
		return false; 
	}
	
	/**
	 * 
	 * @param loginForm
	 * @return
	 */
	public Blogger GET_Blogger(LoginForm loginForm)
	{
		logger.info("Entering DatabaseService:GET_LoginBlogger()");
		
		Blogger blogger = null;
		try {
			String sql = "SELECT * FROM bloggers WHERE user_name = ? AND pass_word = ?";
			blogger = database.queryForObject(sql, (rs, rowNum) -> {
	            Blogger result = new Blogger();	            
	            result.setUsername(rs.getString("user_name"));
	            result.setPassword(rs.getString("pass_word"));
	            return result;
	        }, loginForm.getUsername(), loginForm.getPassword());
			
			logger.info("Exiting DatabaseService:GET_LoginBlogger() with ['blogger']="+blogger.ToString());	
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();			
			
			logger.info("Error finding Blogger with ['Username']="+loginForm.getUsername()+" in database.");
			logger.info("Exiting DatabaseService:GET_LoginBlogger() with ['blogger']=null");	

		}			
		return blogger;
	}
	
	/**
	 * Get all published blogs from database.
	 * @return
	 */
	public List<Blog> GET_Blogs()
	{
		logger.info("Entering DatabaseService:GET_Blogs()");
		
		List<Blog> blogs = new ArrayList<>();
		try {
			String sql = "SELECT * FROM blogs ORDER BY blog_id DESC";
			SqlRowSet record = database.queryForRowSet(sql);				
			while (record.next())
			{
				blogs.add(new Blog(
						record.getInt("blog_id"),
						record.getString("user_name"),
						record.getString("blog_text"),
						record.getString("time_stamp")
					));
			}
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		logger.info("Exiting DatabaseService:GET_Blogs() with "+blogs.size() + " published blogs."); 
		
		return blogs;
	}
	
	/**
	 * 
	 * @param blog_id
	 * @return
	 */
	public Blog GET_Blog(int blog_id)
	{
		Blog blog = null; 
		try {
			String sql = "SELECT * FROM blogs WHERE blog_id = ?";
			blog = database.queryForObject(sql, (rs, rowNum) -> {
				Blog result = new Blog();
	            result.setBlogId(rs.getInt("blog_id"));
	            result.setUsername(rs.getString("user_name"));
	            result.setBlogText(rs.getString("blog_text"));
	            result.setTimestamp(rs.getString("time_stamp"));
	            return result;
	        }, blog_id);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
			logger.info("Exiting Database:GET_Blog() with ['blog']=null");
		}
		return blog;
	}
	
	/**
	 * Return true or false if username already exists in database.
	 * @param username
	 * @return
	 */
	public boolean GET_UsernameExists(String username)
	{
		logger.info("Entering Database:GET_UsernameExists() with ['username']="+username);
		try 
		{
			String sql = "SELECT COUNT(*) FROM bloggers WHERE user_name LIKE ?;";
			int username_exists_result = database.queryForObject(sql, new Object[] { username }, Integer.class);
			if (username_exists_result > 0)
			{
				logger.info("Exiting Database:GET_UsernameExists() as true");
				return true;
			}
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		logger.info("Exiting Database:GET_UsernameExists() as false");
		return false;
	}
	
	/**
	 * Post a comment to database.
	 * @param comment
	 * @return
	 */
	public boolean POST_Comment(Comment comment)
	{
		logger.info("Entering DatabaseService:POST_Comment()");
		int queryResult = -1;
		if (!GET_CommentExists(comment))
		{		
			try 
			{
				timestamp = LocalDateTime.now();  
		        comment.setTimestamp(timestamp.format(formatter));
		        
				String sql = "INSERT INTO comments (blog_id, user_name, comment_text, time_stamp) VALUES (?, ?, ?, ?)";
				queryResult = database.update(sql, comment.getBlogId(), comment.getUsername(), comment.getCommentText(), comment.getTimestamp());
			}
			catch (Exception e)
			{
				logger.error(e.getMessage());
				e.printStackTrace(); 
			}
		}
		
		logger.info("Exiting DatabaseService:POST_Comment() with "+(queryResult > 0));
		
		return (queryResult > 0);
	}
	
	/**
	 * Get all comments for a specific blog.
	 * @param blog_id
	 * @return
	 */
	public List<Comment> GET_Comments(int blog_id)
	{
		logger.info("Entering DatabaseService:GET_Comments() with ['blog_id']="+blog_id);
		
		List<Comment> comments = new ArrayList<>();		
		try
		{
			String sql = "SELECT * FROM comments WHERE blog_id = ? ORDER BY comment_id DESC";
			SqlRowSet record = database.queryForRowSet(sql, blog_id);				
			while (record.next())
			{
				comments.add(new Comment(
						record.getInt("comment_id"),
						record.getInt("blog_id"),
						record.getString("user_name"),
						record.getString("comment_text"),
						record.getString("time_stamp")
					));
			}
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			e.printStackTrace(); 
		}
		
		logger.info("Exiting DatabaseService:GET_Comments() with "+comments.size()+" Comments.");
		
		return comments;
	}
	
	/**
	 * Check if a similar comment already exists in the database.
	 * @param comment
	 * @return
	 */
	public boolean GET_CommentExists(Comment comment)
	{
		logger.info("Entering Database:GET_CommentExists()");
		try 
		{
			String sql = "SELECT COUNT(*) FROM comments WHERE blog_id = ? AND user_name = ? AND comment_text = ?;";
			int comment_exists_result = database.queryForObject(sql, new Object[] { comment.getBlogId(), comment.getUsername(), comment.getCommentText() }, Integer.class);
			if (comment_exists_result > 0)
			{
				logger.info("Exiting Database:GET_CommentExists() as true");
				return true;
			}
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		logger.info("Exiting Database:GET_CommentExists() as false");
		return false;
	}
	
	/**
	 * Get number of comments for a specific blog.
	 * @param blog_id
	 * @return
	 */
	public int GET_CommentCount(int blog_id)
	{
		logger.info("Entering DatabaseService:GET_CommentCount() with ['blog_id']="+blog_id);
		try
		{
			String sql = "SELECT COUNT(*) FROM comments WHERE blog_id LIKE ?;";
			int count = database.queryForObject(sql, new Object[] { blog_id }, Integer.class);
			return count;
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}
}
