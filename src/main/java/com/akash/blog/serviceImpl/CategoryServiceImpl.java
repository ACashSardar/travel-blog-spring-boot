package com.akash.blog.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akash.blog.entity.Category;
import com.akash.blog.repository.CategoryRepository;
import com.akash.blog.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	CategoryRepository categoryRepository;
	
	@Override
	public Category createCategory(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public Category updateCategory(Integer id, Category category) {
		return null;
	}

	@Override
	public void deleteCategory(Integer id) {
		categoryRepository.deleteById(id);
	}

	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Category getByCategoryName(String categoryName) {
		List<Category> categories=categoryRepository.findByCategoryName(categoryName);
		if(categories.size()==0) return null;
		return categories.get(0);
	}


}
