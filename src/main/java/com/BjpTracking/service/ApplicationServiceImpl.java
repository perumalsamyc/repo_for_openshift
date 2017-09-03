package com.BjpTracking.service;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.BjpTracking.Dao.ApplicationDbDao;
import com.BjpTracking.Util.Util;
import com.BjpTracking.model.User;

@Service("applicationService")
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

	@Autowired 
	ApplicationDbDao applicationDbDao; 
	
	public User login(String userName, String password) {
		 password=Util.base64Conv(password, 0);
		 System.out.println("Basee:"+password);
		 User user=applicationDbDao.login(userName, password);		 
		 return user;
	}

	public int saveUser(User user) {
		String password=Util.base64Conv(user.getPassword(), 0);
		user.setPassword(password);
		int count=applicationDbDao.saveUser(user);
		return count; 
	}

	@Override
	public String getRandomString() {
		  SecureRandom random = new SecureRandom();
		  return new BigInteger(130, random).toString(32);		
	}

	
	
	
	
}
