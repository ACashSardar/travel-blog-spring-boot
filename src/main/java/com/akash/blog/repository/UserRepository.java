package com.akash.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akash.blog.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findByEmail(String email);
}
