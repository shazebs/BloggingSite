package com.gcu.models;

public class LoginForm
{
	private String Username;
	private String Password;
	
	// Constructor no params
	public LoginForm()
	{
		this.Username = "";
		this.Password = ""; 
	}
	
	// Constructor w/params
	public LoginForm(String username, String password)
	{
		this.Username = username;
		this.Password = password;
	}
	
	// Getters
	public String getUsername() { return this.Username; }
	public String getPassword() { return this.Password; }
	
	// Setters
	public void setUsername(String x) { this.Username = x; }
	public void setPassword(String x) { this.Password = x; }
}