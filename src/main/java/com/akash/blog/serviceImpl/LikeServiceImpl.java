package com.akash.blog.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akash.blog.entity.Like;
import com.akash.blog.entity.Post;
import com.akash.blog.entity.User;
import com.akash.blog.repository.LikeRepository;
import com.akash.blog.repository.PostRepository;
import com.akash.blog.repository.UserRepository;
import com.akash.blog.service.LikeService;

@Service
public class LikeServiceImpl implements LikeService {

	@Autowired
	LikeRepository likeRepository;

	@Autowired
	PostRepository postRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public Like createLike(Like like, Integer postId, Integer userId) {
		Post post = postRepository.findById(postId).get();
		User user = userRepository.findById(userId).get();
		like.setPost(post);
		like.setUser(user);
		likeRepository.save(like);
		return like;
	}

	@Override
	public void deleteLike(Integer likeId) {
		likeRepository.deleteById(likeId);
	}

	@Override
	public int getLikeId(Integer postId, Integer userId) {
		List<Like> likes = likeRepository.findAll();
		int likeId = -1;
		for (Like like : likes) {
			if (like.getPost().getId() == postId && like.getUser().getId() == userId) {
				likeId = like.getLikeId();
				break;
			}
		}
		return likeId;
	}

}
