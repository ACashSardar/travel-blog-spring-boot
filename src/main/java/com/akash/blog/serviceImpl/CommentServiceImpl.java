package com.akash.blog.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akash.blog.entity.Comment;
import com.akash.blog.entity.Post;
import com.akash.blog.entity.User;
import com.akash.blog.repository.CommentRepository;
import com.akash.blog.repository.PostRepository;
import com.akash.blog.repository.UserRepository;
import com.akash.blog.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Comment createComment(Comment comment, Integer postId, Integer userId) {
		Post post=postRepository.findById(postId).get();
		User user=userRepository.findById(userId).get();
		comment.setPost(post);
		comment.setUser(user);
		commentRepository.save(comment);
		return comment;
	}

	@Override
	public void deleteComment(Integer commentId) {
		Comment comment=commentRepository.findById(commentId).get();
		commentRepository.delete(comment);
	}

}
