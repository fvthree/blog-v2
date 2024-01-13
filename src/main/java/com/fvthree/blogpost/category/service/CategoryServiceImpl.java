package com.fvthree.blogpost.category.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fvthree.blogpost.category.controller.request.CreateCategoryRequest;
import com.fvthree.blogpost.category.controller.response.ListTypedResponse;
import com.fvthree.blogpost.category.dao.impl.Category;
import com.fvthree.blogpost.category.dao.impl.CategoryDao;
import com.fvthree.blogpost.exceptions.DBException;
import com.fvthree.blogpost.exceptions.HTTP400Exception;
import com.fvthree.blogpost.exceptions.RestAPIException;
import com.fvthree.blogpost.post.entity.Post;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
	
	private static final Logger logger = LogManager.getLogger(CategoryServiceImpl.class);
	
	@Autowired
	private CategoryDao categoryDao;

	@Override
	public String createCategory(CreateCategoryRequest request) {
		Category category = null;
		try {
			category = new Category();
			category.setName(request.getName());
			category.setAuthor(request.getAuthor());
			categoryDao.insert(category);
		} catch(DBException e) {
			logger.error("category service impl = {}", e.getMessage());
			throw new DBException(e.getMessage());
		} catch(Exception e) {
			logger.error("service impl = {}", e.getMessage());
			throw new RestAPIException(e.getMessage());
		}
		return "Successfully created a category!";
	}

	@Override
	public ListTypedResponse<Category> findAll() {
		ListTypedResponse<Category> response = new ListTypedResponse<>();
		
		List<Category> categories = categoryDao.findAll(0, 0);
		
		response.setStatus("success");
		response.setMessage("Categories successfully fetched");
		response.setData(categories);
		
		return response;
	}

	@Override
	public String updateCategory(long id , CreateCategoryRequest request) {
		try {
			Category category = categoryDao.findByCategoryId(id)
					.orElseThrow(() -> new HTTP400Exception("category not found."));
			category.setAuthor(request.getAuthor());
			category.setName(request.getName());
			categoryDao.update(id, category);
		} catch (DBException e) {
			logger.error("DB Exception occured = {}", e.getMessage());
			throw new DBException(e.getMessage());
		} catch (Exception e) {
			logger.error("Exception occurend = {}", e.getMessage());
			throw new RestAPIException(e.getMessage());
		}
		return "Successfully updated the category";
	}

	@Override
	public String deleteCategory(Long id) {
		try {
			categoryDao.delete(id);
		} catch (DBException e) {
			logger.error("");
			throw new DBException(e.getMessage());
		} catch (Exception e) {
			logger.error("");
			throw new RestAPIException(e.getMessage());
		}
		return "Successfully deleted the category";
	}

	@Override
	public ListTypedResponse<Post> findAllPostByCategory(long id) {
		ListTypedResponse<Post> response = new ListTypedResponse<>();
		response.setStatus("Success");
		response.setMessage("Successfully fetched all posts.");
		response.setData(categoryDao.findAllPostsByCategory(id));
		return response;
	}

}
