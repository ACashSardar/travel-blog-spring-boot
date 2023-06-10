package com.akash.blog.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.akash.blog.entity.User;
import com.akash.blog.service.FileService;
import com.akash.blog.service.UserService;

@RestController
public class RegisterController {

	@Value("${project.image}")
	private String path;

	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	@GetMapping("/register")
	public ModelAndView getRegistrationPage() {
		User user = new User();
		return new ModelAndView("register", "user", user);
	}

	@PostMapping("/register")
	public ModelAndView registerUser(@ModelAttribute User user, @RequestParam("profilePic") MultipartFile profilePic)
			throws IOException {

		String fileName = "default_profile_pic.jpg";
		if (!profilePic.getOriginalFilename().equals("")) {
			fileName = fileService.uploadImage(path, profilePic);
		}
		user.setProfilePicture(fileName);
		try {
			userService.saveUser(user, true);
			return new ModelAndView("redirect:/");
		} catch (Exception e) {
			return new ModelAndView("redirect:/register?error=true");
		}
	}
}
