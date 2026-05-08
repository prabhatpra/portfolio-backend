package com.prabhat.portfolio.service;

public interface EmailService {

	
	public void sendContactMail(String name, String email, String subject, String message);
	
	 public void sendAutoReply(String name, String email);
		  
}
