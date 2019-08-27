package com.acuity.rdso.sbe_user.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

public class UserModel {
	private String userName;
	private String password;
	private int attempt;
	private String accountStatus;
	private String firstName;
	private String lastName;
	private String email;
	private LocalDateTime accountCreationDate;
	private LocalDateTime lastLogin;
	private boolean locked;
	  
	public UserModel() {
		
	}

	public UserModel(String userName, String password, int attempt, String accountStatus, String firstName,
			String lastName, String email, LocalDateTime accountCreationDate, LocalDateTime lastLogin, boolean locked) {
		super();
		this.userName = userName;
		this.password = password;
		this.attempt = attempt;
		this.accountStatus = accountStatus;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.accountCreationDate = accountCreationDate;
		this.lastLogin = lastLogin;
		this.locked = locked;
	}

	public String getUsername() {
		return userName;
	}

	public void setUsername(String username) {
		this.userName = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAttempt() {
		return attempt;
	}

	public void setAttempt(int attempt) {
		this.attempt = attempt;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getAccountCreationDate() {
		return accountCreationDate;
	}

	public void setAccountCreationDate(LocalDateTime accountCreationDate) {
		this.accountCreationDate = accountCreationDate;
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "UserModel [userName=" + userName + ", password=" + password + ", attempt=" + attempt
				+ ", accountStatus=" + accountStatus + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", accountCreationDate=" + accountCreationDate + ", lastLogin=" + lastLogin
				+ ", locked=" + locked + "]";
	}

}
