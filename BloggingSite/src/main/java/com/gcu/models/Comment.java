package com.gcu.models;

public class Comment
{
	public int CommentId;
	public int BlogId; // the blog associated with this comment
	public String Username;
	public String CommentText;
	public String Timestamp;
	
	public Comment(int blog_id, String comment_text, String user_name)
	{
		this.BlogId = blog_id;
		this.CommentText = comment_text;
		this.Username = user_name;
		this.Timestamp = "n/a";
	}
	
	public Comment(int comment_id, int blog_id, String user_name, String comment_text, String time_stamp)
	{
		this.CommentId = comment_id;
		this.BlogId = blog_id;
		this.Username = user_name;
		this.CommentText = comment_text;
		this.Timestamp = time_stamp;
	}
	
	// get
	public int getBlogId() { return this.BlogId; }
	public String getUsername() { return this.Username; }
	public String getCommentText() { return this.CommentText; }
	public String getTimestamp() { return this.Timestamp; }
	
	// set
	public void setTimestamp(String x) { this.Timestamp = x; }
}
