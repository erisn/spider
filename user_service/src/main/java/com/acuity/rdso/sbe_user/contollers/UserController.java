package com.acuity.rdso.sbe_user.contollers;


import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acuity.rdso.sbe_user.entities.User;
import com.acuity.rdso.sbe_user.entities.UserRole;
import com.acuity.rdso.sbe_user.model.UserModel;
import com.acuity.rdso.sbe_user.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@RestController
@EnableResourceServer
public class UserController {

	private static final Logger Logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;


  @GetMapping(value = "/user/{username}", produces = "application/json")
  @CrossOrigin(origins = "*", allowedHeaders = "*")
  public User getUser(@PathVariable("username") String userName) {
    return userService.findByUserName(userName);
  }

  @PostMapping(value = "/user")
  @CrossOrigin(origins = "*", allowedHeaders = "*")
  public User postUser(@RequestBody UserModel user) {

	  Logger.info("Creating User");
			  
	  String encodedPassword = passwordEncoder.encode(String.valueOf(user.getPassword()));
	    User userLoc = new User();
	    LocalDateTime localDate = LocalDateTime.now();
	    userLoc.setAccountCreationDate(localDate);
	    userLoc.setPassword(encodedPassword);
	    userLoc.setAttempt(0);
	    userLoc.setFirstName(user.getFirstName());
	    userLoc.setLastName(user.getLastName());
	    userLoc.setLocked(false);
	    userLoc.setUsername(user.getUsername());
	    userLoc.setAccountStatus("ACTIVE_STATUS");
	    UserRole role = new UserRole();
        role.setRoleId("1");
	    role.setRoleName("User");
	    userLoc.setUserRole(role);
	    try {
	     userLoc = userService.createUser(userLoc);
	    } catch (Exception e) {
			Logger.error(e.toString());
	    }
	  return userLoc;
  }

  @GetMapping(value = "/health")
  public String healthCheck() {
    return "Healthy";
  }

}

