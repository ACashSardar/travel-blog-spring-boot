package com.akash.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.akash.blog.entity.Like;
import com.akash.blog.service.LikeService;
import com.akash.blog.service.UserService;

@RestController
public class LikeController {

	@Autowired
	LikeService likeService;

	@Autowired
	UserService userService;

	@GetMapping("/posts/like/{postId}/{userId}")
	public void handleLikes(@PathVariable("postId") Integer postId, @PathVariable("userId") Integer userId) {
		if (likeService.getLikeId(postId, userId) == -1) {
			Like like = new Like();
			likeService.createLike(like, postId, userId);
		} else {
			likeService.deleteLike(likeService.getLikeId(postId, userId));
		}
	}
}
