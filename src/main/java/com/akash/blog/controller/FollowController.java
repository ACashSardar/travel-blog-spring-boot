package com.akash.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.akash.blog.service.UserService;

@RestController
public class FollowController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/user/follow/{followerId}/{followingId}")
	public ModelAndView follow(@PathVariable("followerId") Integer followerId, @PathVariable("followingId") Integer followingId) {
		userService.follow(followerId, followingId);
		return new ModelAndView("redirect:/posts/profile/{followingId}/page/0/#user-profile");
	}
	
	@GetMapping("/user/unfollow/{followerId}/{followingId}")
	public ModelAndView unfollow(@PathVariable("followerId") Integer followerId, @PathVariable("followingId") Integer followingId) {
		userService.unfollow(followerId, followingId);
		return new ModelAndView("redirect:/posts/profile/{followingId}/page/0/#user-profile");
	}
}
