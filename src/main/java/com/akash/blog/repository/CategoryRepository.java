package com.akash.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akash.blog.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	List<Category> findByCategoryName(String categoryName);
}
