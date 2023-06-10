package com.akash.blog.service;

import com.akash.blog.entity.Comment;

public interface CommentService {
	Comment createComment(Comment comment, Integer postId, Integer userId);
	void deleteComment(Integer commentId);
}
