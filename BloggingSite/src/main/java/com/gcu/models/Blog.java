package com.gcu.models;

import java.util.List;

public class Blog
{
	public int BlogId;
	public int BloggerId; // the author who wrote this blog
	public String TextContent;		
	public String Timestamp;
	public List<Integer> CommentIds; 
}