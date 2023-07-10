package com.akash.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.akash.blog.service.CategoryService;
import com.akash.blog.service.PostService;

@RestController
public class PageController {

	@Autowired
	PostService postService;

	@Autowired
	CategoryService categoryService;

	@GetMapping("/")
	public ModelAndView home() {
		return new ModelAndView("redirect:/page/0");
	}

	@GetMapping("/contact")
	public ModelAndView contact() {
		return new ModelAndView("contact");
	}

	@GetMapping("/about-us")
	public ModelAndView about() {
		return new ModelAndView("about");
	}
}