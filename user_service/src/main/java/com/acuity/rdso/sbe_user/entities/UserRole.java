package com.acuity.rdso.sbe_user.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Table(name = "user_roles")
public class UserRole {

  @Id
  @Column(name = "user_role_id")
  private String roleId;

  @Column(name = "user_role_name")
  private String roleName;

public UserRole(String roleId, String roleName) {
	
	this.roleId = roleId;
	this.roleName = roleName;
}

public UserRole() {
	
}

public String getRoleId() {
	return roleId;
}

public void setRoleId(String roleId) {
	this.roleId = roleId;
}

public String getRoleName() {
	return roleName;
}

public void setRoleName(String roleName) {
	this.roleName = roleName;
}

@Override
public String toString() {
	return "UserRole [roleId=" + roleId + ", roleName=" + roleName + "]";
}
  
  
}
