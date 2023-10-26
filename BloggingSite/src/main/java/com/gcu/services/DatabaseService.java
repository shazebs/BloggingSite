package com.gcu.services;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.gcu.models.Blogger;
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
	public boolean POST_NewBlogger(RegisterForm registerForm)
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
	 * @param loginForm
	 * @return
	 */
	public Blogger GET_LoginBlogger(LoginForm loginForm)
	{
		logger.info("Entering DatabaseService:GET_LoginBlogger()");
		
		Blogger blogger = null;
		try {
			String sql = "SELECT * FROM bloggers WHERE user_name = ? AND pass_word = ?";
			blogger = database.queryForObject(sql, (rs, rowNum) -> {
	            Blogger result = new Blogger();	            
	            result.setBloggerId(rs.getInt("blogger_id"));
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
}
