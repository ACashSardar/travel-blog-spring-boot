package com.akash.blog.serviceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.akash.blog.entity.Follow;
import com.akash.blog.entity.User;
import com.akash.blog.repository.UserRepository;
import com.akash.blog.service.FollowService;
import com.akash.blog.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	FollowService followService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User saveUser(User user, boolean firstTime) {
		if (firstTime == true)
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User getUserById(Integer id) {
		User user = userRepository.findById(id).get();
		return user;
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public void deleteUser(Integer id) {
		userRepository.deleteById(id);
	}

	@Override
	public void follow(Integer followerId, Integer followingId) {
		User follower = userRepository.findById(followerId).get();
		User following = userRepository.findById(followingId).get();
		Follow follow = new Follow();
		follow.setFrom(follower);
		follow.setTo(following);
		followService.createFollow(follow);
	}

	@Override
	public void unfollow(Integer followerId, Integer followingId) {
		User follower = userRepository.findById(followerId).get();
		User following = userRepository.findById(followingId).get();
		Integer followId = -1;
		for (Follow follow : followService.getAllFollows()) {
			if (follow.getFrom() == follower && follow.getTo() == following) {
				followId = follow.getFollowId();
				break;
			}
		}
		followService.deleteFollow(followId);
	}

	@Override
	public boolean isAFollower(Integer followerId, Integer followingId) {
		User follower = userRepository.findById(followerId).get();
		User following = userRepository.findById(followingId).get();
		for (Follow follow : followService.getFollowByTo(following)) {
			if (follow.getFrom() == follower)
				return true;
		}
		return false;
	}

}
