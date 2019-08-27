package com.acuity.rdso.sbe_user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acuity.rdso.sbe_user.entities.User;


public interface UserRepository extends JpaRepository<User, String> {
	User findByUserName(String username);
	
}