package com.akash.blog.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akash.blog.entity.Follow;
import com.akash.blog.entity.User;
import com.akash.blog.repository.FollowRepository;
import com.akash.blog.service.FollowService;

@Service
public class FollowServiceImpl implements FollowService {

	@Autowired
	FollowRepository followRepository;

	@Override
	public Follow createFollow(Follow follow) {
		return followRepository.save(follow);
	}

	@Override
	public List<Follow> getAllFollows() {
		return followRepository.findAll();
	}

	@Override
	public void deleteFollow(Integer followId) {
		followRepository.deleteById(followId);
	}

	@Override
	public List<Follow> getFollowByFrom(User follower) {
		return followRepository.getFollowByFrom(follower);
	}

	@Override
	public List<Follow> getFollowByTo(User following) {
		return followRepository.getFollowByTo(following);
	}

}
