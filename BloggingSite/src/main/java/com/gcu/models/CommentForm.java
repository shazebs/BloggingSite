package com.gcu.models;

public class CommentForm 
{
	private int BlogId; 
	private String CommentText; 
	
	public CommentForm()
	{
		this.CommentText = "";
	}
		
	public CommentForm(int blog_id)
	{
		this.BlogId = blog_id;
		this.CommentText = ""; 
	}
	
	// get
	public int getBlogId() { return this.BlogId; }
	public String getCommentText() { return this.CommentText; }
	
	// set
	public void setBlogId(int x) { this.BlogId = x; }
	public void setCommentText(String x) { this.CommentText = x; }
}
