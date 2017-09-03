package com.BjpTracking.service;

import com.BjpTracking.model.User;

public interface ApplicationService {

	public User login(String userName, String password);
	
	public int saveUser(User user);
	
	public String getRandomString();
}
