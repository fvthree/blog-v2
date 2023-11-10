package com.fvthree.blogpost.post.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fvthree.blogpost.category.controller.response.ListTypedResponse;
import com.fvthree.blogpost.category.controller.response.TypedResponse;
import com.fvthree.blogpost.dto.CreateBlogPost;
import com.fvthree.blogpost.exceptions.HTTP400Exception;
import com.fvthree.blogpost.exceptions.HTTP404Exception;
import com.fvthree.blogpost.exceptions.RestAPIException;
import com.fvthree.blogpost.post.dao.impl.PostDao;
import com.fvthree.blogpost.post.entity.Post;

@Service
@Transactional
public class PostServiceImpl implements PostService {
	
	private static final Logger logger = LogManager.getLogger(PostServiceImpl.class);

	@Autowired
	private PostDao postDao;
	
	@Override
	public TypedResponse<Post> create(CreateBlogPost req) {
		TypedResponse<Post> typedResponse = new TypedResponse<>();
		Post post = null;
		try {
			post = Post.builder()
					.title(req.getTitle())
					.content(req.getContent())
					.categoryId(req.getCategoryId())
					.author(req.getAuthorId())
					.image(req.getImage())
					.build();
			
			long postId = postDao.insert(post);
			
			postDao.insertCategoryPost(req.getCategoryId(), postId);
			
			postDao.insertCreatedPost(req.getAuthorId(), postId);
			
			typedResponse.setStatus("Success");
			typedResponse.setMessage("Post Successfully created");
			typedResponse.setData(post);
			
		} catch (Exception e) {
			logger.error("error inserting data = {}", e.getMessage());
			throw new RestAPIException(e.getMessage());
		}
		return typedResponse;
	}

	@Override
	public Post update(Long id, CreateBlogPost req) {
		Post postInDB = null;
		try {
			postInDB = postDao.findOptionalPostById(id)
					.orElseThrow(() -> new HTTP404Exception("Post not found."));
			
			// check title unique constraint
			if (!req.getTitle().equals(postInDB.getTitle())) {
				Optional<Post> post = postDao.existsByTitle(req.getTitle());
				if (post.isPresent()) {
					throw new HTTP400Exception("title already exists.");
				}
			}
			
			postInDB.setTitle(req.getTitle());
			postInDB.setImage(req.getImage());
			postInDB.setContent(req.getContent());
			
			postDao.update(id, postInDB);
			
		} catch(Exception e) {
			logger.error("error insert post in database = {}" , e.getMessage());
			throw new RestAPIException(e.getMessage());
		}
		
		return postInDB;
	}

	@Override
	public void remove(Long id) {
	}

	@Override
	public Optional<Post> getPost(Long id) {
		return postDao.findOptionalPostById(id);
	}

	@Override
	public List<Post> getPostByCategory(String category) {
		return postDao.findPostsByCategory(category);
	}

	@Override
	public ListTypedResponse<Post> findAll(int size, int page) {
		ListTypedResponse<Post> typedResponse = new ListTypedResponse<>();
		typedResponse.setStatus("Success");
		typedResponse.setMessage("Successfully retrieve posts");
		typedResponse.setData(postDao.findAll(size, page));
		return typedResponse;
	}

}
