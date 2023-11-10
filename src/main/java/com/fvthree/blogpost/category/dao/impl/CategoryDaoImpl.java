package com.fvthree.blogpost.category.dao.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private static final String FIND_BY_ID = "select category_id, name, author, shares, date_created, last_updated from category where category_id = :category_id";
	private static final String FIND_BY_CATEGORY_NAME = "select category_id, name, author, shares, date_created, last_updated from category where name = :name";
	private static final String FIND_ALL_CATEGORIES = "select category_id, name, author, shares, date_created, last_updated from category";
	
	private static final String INSERT_CATEGORY_NAME = "insert into category(name, author, shares, date_created, last_updated) VALUES (:name, :author, :shares, DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp), DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp))";

	private static final String UPDATE_CATEGORY = "update category set name = :name, author = :author, last_updated = DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp) where category_id = :category_id";
	
	private static final String DELETE_BY_CAT_ID = "delete from category where category_id = :category_id";
	
	private static final String CATEGORY_ID = "category_id";
	
	@Override
	public Category findById(long id) {
		return null;
	}

	@Override
	public List<Category> findAll(int page, int size) {
		return namedParameterJdbcTemplate.query(FIND_ALL_CATEGORIES, new CategoryRowMapper());
	}

	@Override
	public long insert(Category category) {
		GeneratedKeyHolder gkh = new GeneratedKeyHolder();
		var paramMap = new MapSqlParameterSource();
		paramMap.addValue("name", category.getName());
		paramMap.addValue("author", category.getAuthor());
		paramMap.addValue("shares", 0);
		try {
			namedParameterJdbcTemplate.update(INSERT_CATEGORY_NAME, paramMap, gkh);
			return Long.parseLong(String.valueOf(gkh.getKeys().get(CATEGORY_ID))); 
		} catch (Exception e) {
			logger.info("insert exception = {} ", e.getMessage());
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public void update(long id, Category entity) {
		try {
			var mapps = new MapSqlParameterSource();
			mapps.addValue("name", entity.getName());
			mapps.addValue("author", entity.getAuthor());
			mapps.addValue(CATEGORY_ID, id);
			namedParameterJdbcTemplate.update(UPDATE_CATEGORY, mapps);
		} catch (Exception e) {
			logger.error("error in update = {}", e.getMessage());
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public void delete(long id) {
		try {
			var mapps = new MapSqlParameterSource();
			mapps.addValue(CATEGORY_ID, id);
			namedParameterJdbcTemplate.update(DELETE_BY_CAT_ID, mapps);
		} catch (Exception e) {
			logger.error("error in delete = {}", e.getMessage());
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public Optional<Category> findByCategoryName(String name) {
		var mapps = new MapSqlParameterSource();
		mapps.addValue("name", name);
		var category = namedParameterJdbcTemplate.queryForObject(FIND_BY_CATEGORY_NAME, mapps, new CategoryRowMapper());
		return Optional.ofNullable(category);
	}
	

	@Override
	public Optional<Category> findByCategoryId(long id) {
		var mapps = new MapSqlParameterSource();
		mapps.addValue(CATEGORY_ID, id);
		var category = namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, mapps, new CategoryRowMapper());
		return Optional.ofNullable(category);
	}
	
	private static final String GET_ALL_POSTS_BY_CATEGORY = ""
			+ "select p.post_id, p.title, p.category_id, p.content, p.image, p.claps, p.shares, p.num_of_post_views, p.author, p.published, p.date_created, p.last_updated from category_posts cp, post p "
			+ "where cp.post_id = p.post_id and p.category_id = :category_id";

	@Override
	public List<Post> findAllPostsByCategory(long id) {
		var mapps = new MapSqlParameterSource();
		mapps.addValue(CATEGORY_ID, id);
		return namedParameterJdbcTemplate.query(GET_ALL_POSTS_BY_CATEGORY, mapps, new PostRowMapper());
	}

}
