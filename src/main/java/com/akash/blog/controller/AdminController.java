package com.akash.blog.controller;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.akash.blog.entity.Category;
import com.akash.blog.entity.Post;
import com.akash.blog.entity.User;
import com.akash.blog.repository.PostRepository;
import com.akash.blog.service.CategoryService;
import com.akash.blog.service.FileService;
import com.akash.blog.service.PostService;
import com.akash.blog.service.UserService;

@RestController
public class AdminController {
	
	@Value("${project.image}")
	private String path;
	
	@Autowired
	UserService userService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	PostService postService;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	PostRepository postRepository;
	
	@GetMapping("/dashboard/admin")
	public ModelAndView getDashboard(Principal principal) {
		ModelAndView mv=new ModelAndView();
		List<User> users=userService.getAllUsers();
		List<Category> categories=categoryService.getAllCategories();
		List<Post> posts=postRepository.findAll();
		mv.addObject("users", users);
		mv.addObject("categories", categories);
		mv.addObject("posts", posts);
		mv.setViewName("adminDashboard");
		return mv;
	}
	
	@GetMapping("/users/delete/{userId}")
	public ModelAndView deleteUser(@PathVariable("userId") Integer userId) {
		userService.deleteUser(userId);
		return new ModelAndView("redirect:/dashboard/admin");
	}
	
	@GetMapping("/categories/delete/{categoryId}")
	public ModelAndView deleteCategory(@PathVariable("categoryId") Integer categoryId) {
		categoryService.deleteCategory(categoryId);
		return new ModelAndView("redirect:/dashboard/admin");
	}
	
	@GetMapping("/posts/delete2/{id}")
	public ModelAndView deletePostByAdmin(@PathVariable("id") Integer id) throws FileNotFoundException {
		Post post = postService.getById(id);
		String filename = post.getImageName();
		if (fileService.deleteImage(path, filename))
			postService.deletePost(id);
		return new ModelAndView("redirect:/dashboard/admin");
	}
}
