package com.akash.blog.service;

import java.util.List;
import com.akash.blog.entity.Post;
import com.akash.blog.entity.User;
import com.akash.blog.payload.PostResponse;

public interface PostService {

	Post getById(Integer id);
	PostResponse getAllPost(int pageNumber, int pageSize);
	Post savePost(Post post);
	void deletePost(Integer id);
	Post updatePost(Integer id, String title, String body, String shortDesc);
	List<Post> searchByKeywords(String keyword);
	List<Post> getPostByCategory(Integer categoryId);
	PostResponse getPostByCategory(Integer categoryId, int pageNumber, int pageSize);
	PostResponse getPostByUser(Integer userId, int pageNumber, int pageSize);
	PostResponse getPostFromFollowing(User follower, int pageNumber, int pageSize);
}
