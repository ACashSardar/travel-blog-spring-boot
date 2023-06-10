package com.akash.blog.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.akash.blog.entity.Category;
import com.akash.blog.entity.Follow;
import com.akash.blog.entity.Post;
import com.akash.blog.entity.User;
import com.akash.blog.payload.PostResponse;
import com.akash.blog.repository.PostRepository;
import com.akash.blog.repository.UserRepository;
import com.akash.blog.service.PostService;
import com.akash.blog.repository.CategoryRepository;
import com.akash.blog.controller.PostController;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	PostRepository postRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Post getById(Integer id) {
		Post post = postRepository.findById(id).get();
		return post;
	}

	public PostResponse getPostResponse(Page<Post> pagePost, List<Post> myPosts, int pageNumber, int pageSize,
			int totalElements) {
		List<Post> posts = myPosts == null ? pagePost.getContent() : myPosts;
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(posts);
		if (pagePost != null) {
			postResponse.setPageNumber(pagePost.getNumber());
			postResponse.setPageSize(pagePost.getSize());
			postResponse.setLastPage(pagePost.isLast());
			postResponse.setTotalElements(pagePost.getTotalElements());
			postResponse.setTotalPages(pagePost.getTotalPages());
		} else {
			System.out.println(pageNumber + " " + pageSize + " " + totalElements);
			postResponse.setPageNumber(pageNumber);
			postResponse.setPageSize(pageSize);
			postResponse.setLastPage((pageNumber + 1) * pageSize >= totalElements);
			postResponse.setTotalElements(totalElements);
			postResponse.setTotalPages((int) Math.ceil((double) totalElements / pageSize));
		}
		return postResponse;
	}

	@Override
	public PostResponse getAllPost(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Post> pagePost = postRepository.findAll(pageable);
		return getPostResponse(pagePost, null, pageNumber, pageSize, (int) pagePost.getTotalElements());
	}

	@Override
	public Post savePost(Post post) {
		if (post.getId() == null)
			post.setCreatedAt(LocalDateTime.now());
		return postRepository.save(post);
	}

	@Override
	public void deletePost(Integer id) {
		postRepository.deleteById(id);
	}

	@Override
	public List<Post> searchByKeywords(String keyword) {
		List<Post> titles = postRepository.findByTitleContaining(keyword);
		List<Post> bodies = postRepository.findByBodyContaining(keyword);
		List<Post> shortDescs = postRepository.findByShortDescContaining(keyword);
		Set<Post> uniques = new HashSet<>();
		titles.forEach(elem -> uniques.add(elem));
		bodies.forEach(elem -> uniques.add(elem));
		shortDescs.forEach(elem -> uniques.add(elem));
		List<Post> result = new ArrayList<>();
		for (Post p : uniques)
			result.add(p);
		return result;
	}

	@Override
	public PostResponse getPostByCategory(Integer categoryId, int pageNumber, int pageSize) {
		Category category = categoryRepository.findById(categoryId).get();
		Pageable paging = PageRequest.of(pageNumber, pageSize);
		int totalElements = postRepository.findByCategory(category).size();
		List<Post> posts = postRepository.findByCategory(category, paging);
		return getPostResponse(null, posts, pageNumber, pageSize, totalElements);
	}

	@Override
	public List<Post> getPostByCategory(Integer categoryId) {
		Category category = categoryRepository.findById(categoryId).get();
		List<Post> posts = postRepository.findByCategory(category);
		return posts;
	}

	@Override
	public PostResponse getPostByUser(Integer userId, int pageNumber, int pageSize) {
		User user = userRepository.findById(userId).get();
		Pageable paging = PageRequest.of(pageNumber, pageSize);
		int totalElements = postRepository.findByUser(user).size();
		List<Post> posts = postRepository.findByUser(user, paging);
		return getPostResponse(null, posts, pageNumber, pageSize, totalElements);
	}

	@Override
	public Post updatePost(Integer id, String title, String body, String shortDesc) {
		Post updatedPost = postRepository.findById(id).get();
		updatedPost.setTitle(title);
		updatedPost.setBody(body);
		updatedPost.setShortDesc(shortDesc);
		updatedPost.setHtml(PostController.markdownToHTML(body));
		postRepository.save(updatedPost);
		return updatedPost;
	}

	@Override
	public PostResponse getPostFromFollowing(User follower, int pageNumber, int pageSize) {
		List<Post> allPosts = new ArrayList<>();
		for (Follow follow : follower.getFollowing()) {
			User following = follow.getTo();
			for (Post post : following.getPosts()) {
				allPosts.add(post);
			}
		}
		List<Post> posts = new ArrayList<>();
		for (int i = pageNumber * pageSize; i < Math.min(allPosts.size(), (pageNumber + 1) * pageSize); i++) {
			posts.add(allPosts.get(i));
		}
		int totalElements = allPosts.size();
		return getPostResponse(null, posts, pageNumber, pageSize, totalElements);
	}

}
