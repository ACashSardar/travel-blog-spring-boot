package com.akash.blog.service;

import java.util.List;

import com.akash.blog.entity.Category;

public interface CategoryService {
	Category createCategory(Category category);
	Category updateCategory(Integer id,Category category);
	void deleteCategory(Integer id);
	List<Category> getAllCategories();
	Category getByCategoryName(String categoryName);
}
