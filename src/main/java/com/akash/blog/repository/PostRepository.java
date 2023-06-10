package com.akash.blog.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.akash.blog.entity.Category;
import com.akash.blog.entity.Post;
import com.akash.blog.entity.User;

public interface PostRepository extends JpaRepository<Post, Integer>{
	List<Post> findByTitleContaining(String title);
	List<Post> findByBodyContaining(String keyword);
	List<Post> findByUser(User user);
	List<Post> findByUser(User user,Pageable paging);
	List<Post> findByCategory(Category category);
	List<Post> findByCategory(Category category,Pageable paging);
	List<Post> findByShortDescContaining(String keyword);
}
