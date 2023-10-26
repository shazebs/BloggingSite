package com.gcu.models;

public class RegisterForm 
{
	private String Username; 
	private String Password1;
	private String Password2;
	
	public RegisterForm() 
	{
		this.Username = "";
		this.Password1 = "";
		this.Password2 = ""; 
	}
	
	public RegisterForm(String username, String pass1, String pass2)
	{
		this.Username = username;
		this.Password1 = pass1;
		this.Password2 = pass2;
	}
	
	// get
	public String getUsername() { return this.Username; }
	public String getPassword1() { return this.Password1; }
	public String getPassword2() { return this.Password2; }
	
	// set
	public void setUsername(String x) { this.Username = x; }
	public void setPassword1(String x) { this.Password1 = x; }
	public void setPassword2(String x) { this.Password2 = x; }
	
	// methods
	public String ToString()
	{
		return String.format("['Username']=%s, ['Password1']=%s, ['Password2']=%s", 
				this.Username, this.Password1, this.Password2
		);
	}
}
