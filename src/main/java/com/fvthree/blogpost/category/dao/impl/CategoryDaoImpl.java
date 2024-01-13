package com.fvthree.blogpost.category.dao.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.fvthree.blogpost.exceptions.DBException;
import com.fvthree.blogpost.post.entity.Post;
import com.fvthree.blogpost.rowmappers.CategoryRowMapper;
import com.fvthree.blogpost.rowmappers.PostRowMapper;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public class CategoryDaoImpl implements CategoryDao {
	
	private static final Logger logger = LogManager.getLogger(CategoryDaoImpl.class);
	
	@Autowired
	private final JdbcClient jdbcClient;

	public CategoryDaoImpl(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	private static final String CATEGORY_ID = "category_id";
	
	@Override
	public Category findById(long id) {
		return null;
	}

	private static final String FIND_ALL_CATEGORIES = "select category_id, name, author, shares, date_created, last_updated from category";


	@Override
	public List<Category> findAll(int page, int size) {
		return jdbcClient.sql(FIND_ALL_CATEGORIES).query(new CategoryRowMapper()).list();
	}

	private static final String INSERT_CATEGORY_NAME = "insert into category(name, author, shares, date_created, last_updated) VALUES (:name, :author, :shares, DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp), DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp))";

	@Override
	public long insert(Category category) {
		GeneratedKeyHolder gkh = new GeneratedKeyHolder();
		try {
			jdbcClient.sql(INSERT_CATEGORY_NAME)
					.param("name", category.getName())
					.param("author", category.getAuthor())
					.param("shares", 0)
					.update(gkh);
			return Long.parseLong(String.valueOf(gkh.getKeys().get(CATEGORY_ID))); 
		} catch (Exception e) {
			logger.info("insert exception = {} ", e.getMessage());
			throw new DBException(e.getMessage());
		}
	}

	private static final String UPDATE_CATEGORY = "update category set name = :name, author = :author, last_updated = DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp) where category_id = :category_id";


	@Override
	public void update(long id, Category entity) {
		try {
			jdbcClient.sql(UPDATE_CATEGORY)
					.param("name", entity.getName())
					.param("author", entity.getAuthor())
					.param(CATEGORY_ID, id)
					.update();
		} catch (Exception e) {
			logger.error("error in update = {}", e.getMessage());
			throw new DBException(e.getMessage());
		}
	}

	private static final String DELETE_BY_CAT_ID = "delete from category where category_id = :category_id";


	@Override
	public void delete(long id) {
		try {
			jdbcClient.sql(DELETE_BY_CAT_ID).param(CATEGORY_ID, id).update();
		} catch (Exception e) {
			logger.error("error in delete = {}", e.getMessage());
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public Optional<Category> findByCategoryName(String name) {
		return jdbcClient.sql(FIND_BY_CATEGORY_NAME).param("name", name).query(new CategoryRowMapper()).optional();
	}

	private static final String FIND_BY_ID = "select category_id, name, author, shares, date_created, last_updated from category where category_id = :category_id";

	@Override
	public Optional<Category> findByCategoryId(long id) {
		return jdbcClient.sql(FIND_BY_ID).param(CATEGORY_ID, id).query(new CategoryRowMapper()).optional();
	}
	
	private static final String GET_ALL_POSTS_BY_CATEGORY = ""
			+ "select p.post_id, p.title, p.category_id, p.content, p.image, p.claps, p.shares, p.num_of_post_views, p.author, p.published, p.date_created, p.last_updated from category_posts cp, post p "
			+ "where cp.post_id = p.post_id and p.category_id = :category_id";

	private static final String FIND_BY_CATEGORY_NAME = "select category_id, name, author, shares, date_created, last_updated from category where name = :name";

	@Override
	public List<Post> findAllPostsByCategory(long id) {
		return jdbcClient.sql(GET_ALL_POSTS_BY_CATEGORY).param(CATEGORY_ID, id).query(new PostRowMapper()).list();
	}

}
