package com.fvthree.blogpost.post.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.fvthree.blogpost.base.dao.BaseDao;
import com.fvthree.blogpost.post.entity.Post;

@Repository
public interface PostDao extends BaseDao<Post> {
	
	Optional<Post> existsByTitle(String title);
	Optional<Post> findOptionalPostById(long id);
	List<Post> findPostsByCategory(String category);
	
	void insertCreatedPost(int userId, long postId);
	void insertCategoryPost(int categoryId, long postId);
}
