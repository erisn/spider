package com.acuity.rdso.sbe_user.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acuity.rdso.sbe_user.contollers.UserController;
import com.acuity.rdso.sbe_user.entities.User;
import com.acuity.rdso.sbe_user.repositories.UserRepository;

@Service
public class UserService {
	
	private static final Logger Logger = LoggerFactory.getLogger(UserService.class);

	@Autowired 
	UserRepository userRepository;
	
	public List<User> getAllUsers(){
		Logger.info("Fetching all users");
		List<User> users = userRepository.findAll();
		return users;
	}
	
	public User findByUserName(String username){
		Logger.info("Finding user");
		User user = userRepository.findByUserName(username);
		return user;
	}
	
	
	public User createUser(User user){
		Logger.info("Creating User");
		User userLoc = userRepository.save(user);
		return userLoc;
	}
	

}
