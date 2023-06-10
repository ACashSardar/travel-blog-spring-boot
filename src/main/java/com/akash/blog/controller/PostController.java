package com.akash.blog.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.akash.blog.config.AppConstants;
import com.akash.blog.entity.Category;
import com.akash.blog.entity.Post;
import com.akash.blog.entity.User;
import com.akash.blog.payload.PostResponse;
import com.akash.blog.service.CategoryService;
import com.akash.blog.service.FileService;
import com.akash.blog.service.PostService;
import com.akash.blog.service.UserService;

@RestController
public class PostController {

	@Value("${project.image}")
	private String path;

	@Autowired
	private PostService postService;

	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	@Autowired
	private CategoryService categoryService;

	public List<Integer> getLikedPostsByUser(Principal principal) {
		List<Integer> likedPostsByUser = null;
		if (principal != null) {
			String email = principal.getName();
			likedPostsByUser = userService.findUserByEmail(email).getLikes().stream()
					.map(like -> like.getPost().getId()).collect(Collectors.toList());
		}
		return likedPostsByUser;
	}

	@GetMapping("/page/{pageNumber}")
	public ModelAndView getAllPosts(@PathVariable("pageNumber") Integer pageNumber, Principal principal) {
		ModelAndView mv = new ModelAndView();
		PostResponse postResponse = postService.getAllPost(pageNumber, AppConstants.PAGE_SIZE);
		List<Post> posts = postResponse.getContent();
		List<Category> categories = categoryService.getAllCategories();
		mv.addObject("likedPostsByUser", getLikedPostsByUser(principal));
		mv.addObject("categories", categories);
		mv.addObject("pagination", true);
		mv.addObject("posts", posts);
		mv.addObject("pageNo", postResponse.getPageNumber());
		mv.addObject("totalPages", postResponse.getTotalPages());
		mv.addObject("isLast", postResponse.isLastPage());
		mv.addObject("baseURL", "");
		mv.setViewName("home");
		return mv;
	}

	@GetMapping("/posts/{id}")
	public ModelAndView getPostById(@PathVariable("id") Integer id, Principal principal) {
		Post post = postService.getById(id);
		ModelAndView mv = new ModelAndView();
		List<Post> recommendedPosts = postService.getPostByCategory(post.getCategory().getId());
		Collections.shuffle(recommendedPosts);
		mv.addObject("post", post);
		mv.addObject("editable", false);
		mv.addObject("likedPostsByUser", getLikedPostsByUser(principal));
		mv.addObject("recommendedPosts", recommendedPosts);
		mv.setViewName("post");
		return mv;
	}

	@GetMapping("/posts/edit/{id}")
	public ModelAndView editPost(@PathVariable("id") Integer id, Principal principal) {
		Post post = postService.getById(id);
		ModelAndView mv = new ModelAndView();
		List<Post> recommendedPosts = postService.getPostByCategory(post.getCategory().getId());
		mv.addObject("post", post);
		mv.addObject("editable", true);
		mv.addObject("likedPostsByUser", getLikedPostsByUser(principal));
		mv.addObject("recommendedPosts", recommendedPosts);
		mv.setViewName("post");
		return mv;
	}

	@PostMapping("/posts/update/{id}")
	public ModelAndView updatePost(@PathVariable("id") Integer id, @RequestParam("title") String title,
			@RequestParam("shortDesc") String shortDesc, @RequestParam("body") String body, Principal principal) {
		Post post = postService.getById(id);
		ModelAndView mv = new ModelAndView();
		List<Post> recommendedPosts = postService.getPostByCategory(post.getCategory().getId());
		if (!title.equals("")) {
			postService.updatePost(id, title, body, shortDesc);
		}
		mv.addObject("post", post);
		mv.addObject("editable", false);
		mv.addObject("likedPostsByUser", getLikedPostsByUser(principal));
		mv.addObject("recommendedPosts", recommendedPosts);
		mv.setViewName("post");
		return mv;
	}

	@GetMapping("/posts/new")
	public ModelAndView getNewPostPage(Principal principal) {
		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}
		ModelAndView mv = new ModelAndView();
		User user = userService.findUserByEmail(principal.getName());
		Post post = new Post();
		post.setUser(user);
		List<Category> categories = categoryService.getAllCategories();
		Collections.reverse(categories);
		mv.addObject("categories", categories);
		mv.addObject("post", post);
		mv.setViewName("newPost");
		return mv;
	}

	@PostMapping("/posts/new")
	public ModelAndView saveNewPost(@ModelAttribute Post post, @RequestParam("image") MultipartFile image,
			@RequestParam("catg") String catg, @RequestParam("newCatg") String newCatg) throws IOException {

		String fileName = fileService.uploadImage(path, image);
		post.setImageName(fileName);
		if (!newCatg.equals(""))
			catg = newCatg;
		Category category = categoryService.getByCategoryName(catg);
		if (category == null) {
			category = new Category();
			category.setCategoryName(catg);
			categoryService.createCategory(category);
		}
		post.setCategory(category);
		post.setHtml(markdownToHTML(post.getBody()));
		postService.savePost(post);

		return new ModelAndView("redirect:/posts/" + post.getId());
	}

	public static String markdownToHTML(String markdown) {
		Parser parser = Parser.builder().build();
		Node document = parser.parse(markdown);
		HtmlRenderer renderer = HtmlRenderer.builder().build();
		return renderer.render(document);
	}

	@GetMapping(value = "/posts/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void serveImage(@PathVariable("imageName") String imageName, HttpServletResponse response)
			throws IOException {
		InputStream resource = fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}

	@GetMapping("/posts/delete/{id}")
	public ModelAndView deletePost(@PathVariable("id") Integer id) throws FileNotFoundException {
		Post post = postService.getById(id);
		String filename = post.getImageName();
		if (fileService.deleteImage(path, filename))
			postService.deletePost(id);
		return new ModelAndView("redirect:/");
	}
	
	@PostMapping("/posts/search")
	public ModelAndView searchByKeywords(@RequestParam("keyword") String keyword, Principal principal) {
		ModelAndView mv = new ModelAndView();
		List<Post> posts = postService.searchByKeywords(keyword);
		List<Category> categories = categoryService.getAllCategories();
		mv.addObject("likedPostsByUser", getLikedPostsByUser(principal));
		mv.addObject("categories", categories);
		mv.addObject("posts", posts);
		mv.setViewName("home");
		return mv;
	}

	@GetMapping("/posts/category/{categoryId}/page/{pageNumber}")
	public ModelAndView getPostByCategory(@PathVariable("categoryId") Integer categoryId,
			@PathVariable("pageNumber") Integer pageNumber, Principal principal) {
		ModelAndView mv = new ModelAndView();
		PostResponse postResponse = postService.getPostByCategory(categoryId, pageNumber, AppConstants.PAGE_SIZE);
		List<Post> posts = postResponse.getContent();
		List<Category> categories = categoryService.getAllCategories();
		mv.addObject("likedPostsByUser", getLikedPostsByUser(principal));
		mv.addObject("categories", categories);
		mv.addObject("pagination", true);
		mv.addObject("pageNo", postResponse.getPageNumber());
		mv.addObject("totalPages", postResponse.getTotalPages());
		mv.addObject("isLast", postResponse.isLastPage());
		mv.addObject("posts", posts);
		mv.addObject("baseURL", "/posts/category/" + categoryId);
		mv.setViewName("home");
		return mv;
	}

	@GetMapping("/posts/profile/{userId}/page/{pageNumber}")
	public ModelAndView getUserProfile(@PathVariable("userId") Integer userId,
			@PathVariable("pageNumber") Integer pageNumber, Principal principal) {
		ModelAndView mv = new ModelAndView();
		User user = userService.getUserById(userId);
		PostResponse postResponse = postService.getPostByUser(userId, pageNumber, AppConstants.PAGE_SIZE);
		List<Post> posts = postResponse.getContent();
		List<Category> categories = categoryService.getAllCategories();
		mv.addObject("likedPostsByUser", getLikedPostsByUser(principal));
		mv.addObject("categories", categories);
		mv.addObject("pagination", true);
		mv.addObject("pageNo", postResponse.getPageNumber());
		mv.addObject("totalPages", postResponse.getTotalPages());
		mv.addObject("isLast", postResponse.isLastPage());
		mv.addObject("posts", posts);
		mv.addObject("userProfile", user);
		mv.addObject("baseURL", "/posts/profile/" + userId);
		if (principal != null) {
			int followerId = userService.findUserByEmail(principal.getName()).getId();
			mv.addObject("isAFollower", userService.isAFollower(followerId, userId));
		}
		mv.setViewName("home");
		return mv;
	}

	@GetMapping("/posts/profile/email/{email}")
	public ModelAndView getDashboard(@PathVariable("email") String email) {
		User user = userService.findUserByEmail(email);
		return new ModelAndView("redirect:/posts/profile/" + user.getId() + "/page/0/#user-profile");
	}

	@GetMapping("/posts/following/page/{pageNumber}")
	public ModelAndView getPostsFromFollowing(@PathVariable("pageNumber") Integer pageNumber, Principal principal) {
		ModelAndView mv = new ModelAndView();
		User follower = userService.findUserByEmail(principal.getName());
		PostResponse postResponse = postService.getPostFromFollowing(follower, pageNumber, AppConstants.PAGE_SIZE);
		List<Post> posts = postResponse.getContent();
		List<Category> categories = categoryService.getAllCategories();
		mv.addObject("likedPostsByUser", getLikedPostsByUser(principal));
		mv.addObject("categories", categories);
		mv.addObject("pagination", true);
		mv.addObject("pageNo", postResponse.getPageNumber());
		mv.addObject("totalPages", postResponse.getTotalPages());
		mv.addObject("isLast", postResponse.isLastPage());
		mv.addObject("posts", posts);
		mv.addObject("baseURL", "/posts/following");
		mv.addObject("fromFollowing", true);
		mv.setViewName("home");
		return mv;
	}
}
