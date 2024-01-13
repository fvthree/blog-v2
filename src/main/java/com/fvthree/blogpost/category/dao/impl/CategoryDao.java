package com.fvthree.blogpost.category.dao.impl;

import java.util.List;
import java.util.Optional;

import com.fvthree.blogpost.base.dao.BaseDao;
import com.fvthree.blogpost.post.entity.Post;

public interface CategoryDao extends BaseDao<Category> {
	Optional<Category> findByCategoryName(String name);
	Optional<Category> findByCategoryId(long id);
	List<Post> findAllPostsByCategory(long id);
}
