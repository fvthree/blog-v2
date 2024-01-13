package com.fvthree.blogpost.post.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fvthree.blogpost.category.controller.response.ListTypedResponse;
import com.fvthree.blogpost.category.controller.response.TypedResponse;
import com.fvthree.blogpost.dto.CreateBlogPost;
import com.fvthree.blogpost.post.entity.Post;

@Service
@Transactional
public interface PostService {
	TypedResponse<Post> create(CreateBlogPost req);
	Post update(Long id, CreateBlogPost req);
	void remove(Long id);
	Optional<Post> getPost(Long id);
	List<Post> getPostByCategory(String category);
	ListTypedResponse<Post> findAll(int size, int page);
}
