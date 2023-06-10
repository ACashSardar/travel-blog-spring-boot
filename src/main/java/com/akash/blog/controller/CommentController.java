package com.akash.blog.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.akash.blog.entity.Comment;
import com.akash.blog.service.CommentService;

@RestController
public class CommentController {

	@Autowired
	private CommentService commentService;

	@PostMapping("/posts/comment/{postId}/{userId}")
	public ModelAndView createComment(@RequestParam("comment") String cmt, @PathVariable("postId") Integer postId, @PathVariable("userId") Integer userId) {
		Comment comment = new Comment();
		comment.setContent(cmt);
		comment.setCreatedAt(LocalDateTime.now());
		commentService.createComment(comment, postId, userId);
		return new ModelAndView("redirect:/posts/" + postId);
	}

	@GetMapping("/comments/delete/{commentId}/{postId}")
	public ModelAndView deleteComment(@PathVariable("commentId") Integer commentId, @PathVariable("postId") Integer postId) {
		commentService.deleteComment(commentId);
		return new ModelAndView("redirect:/posts/" + postId);
	}

}
