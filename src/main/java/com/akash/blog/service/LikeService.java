package com.akash.blog.service;

import com.akash.blog.entity.Like;

public interface LikeService {
	Like createLike(Like like, Integer postId, Integer userId);
	void deleteLike(Integer likeId);
	int getLikeId(Integer postId, Integer userId);
}
