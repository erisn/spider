package com.acuity.rdso.sbe_user.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.CascadeType;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Table(name = "users")
public class User implements Serializable {

 private static final long serialVersionUID = 1701325902333490974L;


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "user_id")
  private int userId;


  @Column(name = "user_name")
  private String userName;

  @Column(name = "user_photo")
  private String userPhoto;

  @Column(name = "user_password")
  private String password;

  @OneToOne
  @JoinColumn(name = "user_role_id", referencedColumnName = "user_role_id")
  private UserRole userRole;

  @Column(name = "user_attempt")
  private int attempt;

  @Column(name = "user_account_status")
  private String accountStatus;

  @Column(name = "user_first_name")
  private String firstName;

  @Column(name = "user_last_name")
  private String lastName;

  @Column(name = "user_email")
  private String email;

  @Column(name = "user_creation_date")
  private LocalDateTime accountCreationDate;

  @Column(name = "user_last_login")
  private LocalDateTime lastLogin;

  @Column(name = "user_locked")
  private boolean locked;

public User() {

}

public User(int userId, String userName, String userPhoto, String password, UserRole userRole, int attempt,
		String accountStatus, String firstName, String lastName, String email, LocalDateTime accountCreationDate,
		LocalDateTime lastLogin, boolean locked) {
	super();
	this.userId = userId;
	this.userName = userName;
	this.userPhoto = userPhoto;
	this.password = password;
	this.userRole = userRole;
	this.attempt = attempt;
	this.accountStatus = accountStatus;
	this.firstName = firstName;
	this.lastName = lastName;
	this.email = email;
	this.accountCreationDate = accountCreationDate;
	this.lastLogin = lastLogin;
	this.locked = locked;
}

public int getUserId() {
	return userId;
}

public void setUserId(int userId) {
	this.userId = userId;
}

public String getUsername() {
	return userName;
}

public void setUsername(String username) {
	this.userName = username;
}

public String getUserPhoto() {
	return userPhoto;
}

public void setUserPhoto(String userPhoto) {
	this.userPhoto = userPhoto;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

public UserRole getUserRole() {
	return userRole;
}

public void setUserRole(UserRole userRole) {
	this.userRole = userRole;
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

public static long getSerialversionuid() {
	return serialVersionUID;
}

@Override
public String toString() {
	return "User [userId=" + userId + ", userName=" + userName + ", userPhoto=" + userPhoto + ", password=" + password
			+ ", userRole=" + userRole + ", attempt=" + attempt + ", accountStatus=" + accountStatus + ", firstName="
			+ firstName + ", lastName=" + lastName + ", email=" + email + ", accountCreationDate=" + accountCreationDate
			+ ", lastLogin=" + lastLogin + ", locked=" + locked + "]";
}



}
