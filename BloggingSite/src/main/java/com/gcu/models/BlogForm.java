package com.gcu.models;

public class BlogForm 
{
	private String Username;
	private String BlogText;
	private String Timestamp;
	
	public BlogForm()
	{
		this.Username = "";
		this.BlogText = "";
		this.Timestamp = "";
	}
	
	public BlogForm(String username, String blogText, String timestamp)
	{
		this.Username = username;
		this.BlogText = blogText;
		this.Timestamp = timestamp;
	}
	
	// getters
	public String getBlogText() { return this.BlogText; }
	public String getUsername() { return this.Username; }
	public String getTimestamp() { return this.Timestamp; }
	
	// setters
	public void setBlogText(String x) { this.BlogText = x; }
	public void setUsername(String x) { this.Username = x; }
	public void setTimestamp(String x) { this.Timestamp = x; }
}
