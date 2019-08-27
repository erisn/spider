package com.acuity.rdso.sbe_user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginForm {
  @NotNull
  private String username;
  @NotNull
  private String password;
  
@Override
public String toString() {
	return "LoginForm [username=" + username + ", password=" + password + "]";
}
public LoginForm() {
	super();
}
public LoginForm(@NotNull String username, @NotNull String password) {
	super();
	this.username = username;
	this.password = password;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
  
  
}
