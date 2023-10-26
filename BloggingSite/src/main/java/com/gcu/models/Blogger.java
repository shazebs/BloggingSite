package com.gcu.models;

import java.util.List;

public class Blogger 
{
	public int BloggerId; 
	public String Username;
	public String Password; 
	
//	public String PasswordHash; 
//	public List<Integer> PublishedBlogIds; 
//	public List<Integer> FavoriteBlogIds;\
	
	// get
	public int getBloggerId() { return this.BloggerId; }
	public String getUsername() { return this.Username; }
	public String getPassword() { return this.Password; }
	
	// set
	public void setBloggerId(int x) { this.BloggerId = x; }
	public void setUsername(String x) { this.Username = x; }
	public void setPassword(String x) { this.Password = x; }
	
	// methods
	public String ToString()
	{
		return String.format("['BloggerId']=%d, ['Username']=%s, ['Password']=%s", 
				this.BloggerId, this.Username, this.Password
		);
	}
}