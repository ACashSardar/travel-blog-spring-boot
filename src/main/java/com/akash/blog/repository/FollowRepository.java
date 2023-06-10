package com.akash.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akash.blog.entity.Follow;
import com.akash.blog.entity.User;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
	List<Follow> getFollowByFrom(User follower);
	List<Follow> getFollowByTo(User following);
}
