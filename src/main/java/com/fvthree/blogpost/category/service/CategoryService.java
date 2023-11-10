package com.fvthree.blogpost.category.service;

import com.fvthree.blogpost.category.controller.request.CreateCategoryRequest;
import com.fvthree.blogpost.category.controller.response.ListTypedResponse;
import com.fvthree.blogpost.category.dao.impl.Category;
import com.fvthree.blogpost.post.entity.Post;

public interface CategoryService {
	
	String createCategory(CreateCategoryRequest request);
	ListTypedResponse<Category> findAll();
	String updateCategory(long id, CreateCategoryRequest request);
	String deleteCategory(Long id);
	ListTypedResponse<Post> findAllPostByCategory(long id);
}
