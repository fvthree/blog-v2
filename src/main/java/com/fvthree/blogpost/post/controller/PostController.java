package com.fvthree.blogpost.post.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fvthree.blogpost.category.controller.response.ListTypedResponse;
import com.fvthree.blogpost.category.controller.response.TypedResponse;
import com.fvthree.blogpost.dto.CreateBlogPost;
import com.fvthree.blogpost.post.entity.Post;
import com.fvthree.blogpost.post.service.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/post")
public class PostController extends PostAbstractController {
	
	@Autowired
	private PostService postService;
	
	public PostController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping
	public ResponseEntity<ListTypedResponse<Post>> findAllPost(
			@RequestParam(value="size", defaultValue="10", required=false) int size,
			@RequestParam(value="page", defaultValue="1", required=false) int page) {
		return new ResponseEntity<>(postService.findAll(size, page), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<TypedResponse<Post>> createPost(@Valid @RequestBody CreateBlogPost request) {
		return new ResponseEntity<>(postService.create(request), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Post> update(@Valid @RequestBody CreateBlogPost request , @PathVariable(name="id") Long id) {
		return new ResponseEntity<>(postService.update(id, request), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> remove(@PathVariable(name="id") Long id) {
		postService.remove(id);
		return new ResponseEntity<>("Post deleted successfully.", HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Post>> getPostById(@PathVariable(name="id") Long id) {
		return new ResponseEntity<>(postService.getPost(id), HttpStatus.OK);
	}
	
	@GetMapping("/category/{category}")
	public ResponseEntity<List<Post>> getPostsByCategory(@PathVariable(name="category") String category) {
		return new ResponseEntity<>(postService.getPostByCategory(category), HttpStatus.OK);
	}
	
}
