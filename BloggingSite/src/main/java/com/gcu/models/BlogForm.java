package com.gcu.models;

public class BlogForm 
{
	private String Text;
	
	public BlogForm()
	{
		this.Text = "";
	}
	
	public BlogForm(String text)
	{
		this.Text = text;
	}
	
	// getters
	public String getText() { return this.Text; }
	
	// setters
	public void setText(String x) { this.Text = x; }
}
