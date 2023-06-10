package com.akash.blog.service;

import java.util.List;

import com.akash.blog.entity.Follow;
import com.akash.blog.entity.User;

public interface FollowService {
	Follow createFollow(Follow follow);
	List<Follow> getAllFollows();
	void deleteFollow(Integer followId);
	List<Follow> getFollowByFrom(User follower);
	List<Follow> getFollowByTo(User following);
}
