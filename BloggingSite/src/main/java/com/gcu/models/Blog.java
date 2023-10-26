package com.gcu.models;

import java.util.List;

public class Blog
{
	public int BlogId;
	public String Username; // the author who wrote this blog
	public String BlogText;		
	public String Timestamp;
	
	public Blog()
	{
	}
	
	public Blog(int blog_id, String user_name, String blog_text, String time_stamp)
	{
		this.BlogId = blog_id;
		this.Username = user_name;
		this.BlogText = blog_text;
		this.Timestamp = time_stamp;
	}
	
	// get 
	public int getBlogId() { return this.BlogId; }
	public String getUsername() { return this.Username; }
	public String getBlogText() { return this.BlogText; }
	public String getTimestamp() { return this.Timestamp; }
	
	// set
	public void setBlogId(int x) { this.BlogId = x; }
	public void setUsername(String x) { this.Username = x; }
	public void setBlogText(String x) { this.BlogText = x; }
	public void setTimestamp(String x) { this.Timestamp = x; }

	
	// methods
	public String getLimitedBlog() 
	{
		int maxLength = 300; 
		if (this.BlogText.length() > maxLength) 
		{
            String limitedString = this.BlogText.substring(0, maxLength - 3) + "...";
            return limitedString;
        }
		return this.BlogText; 
	}
}