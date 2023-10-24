package com.gcu.models;

import java.util.List;

public class Blogger 
{
	public int BloggerId; 
	public String Username;
	public String Password; 
	public String PasswordHash; 
	public List<Integer> PublishedBlogIds; 
	public List<Integer> FavoriteBlogIds;
}
