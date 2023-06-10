package com.akash.blog.service;

import java.util.List;

import com.akash.blog.entity.User;

public interface UserService{
	User saveUser(User user,boolean firstTime);
	User findUserByEmail(String email);
	User getUserById(Integer id);
	List<User> getAllUsers();
	void deleteUser(Integer id);
	void follow(Integer followerId, Integer followingId);
	void unfollow(Integer followerId, Integer followingId);
	boolean isAFollower(Integer followerId, Integer followingId);
}
