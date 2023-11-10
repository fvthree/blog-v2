package com.fvthree.blogpost.post.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.fvthree.blogpost.post.entity.Post;
import com.fvthree.blogpost.rowmappers.PostRowMapper;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public class PostDaoImpl implements PostDao {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public PostDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.jdbcTemplate = namedParameterJdbcTemplate;
	}
	
	private static final String FIND_BY_TITLE = "select post_id, title, category, "
			+ "content_one, content_two, image, author, date_created, last_updated "
			+ "from post where title = :title";
	
	private static final String FIND_BY_CATEGORY = "select post_id, title, category, "
			+ "content_one, content_two, image, author, date_created, last_updated "
			+ "from post where category = :category";
	
	private static final String FIND_ALL_POST = "select "
			+ "post_id, title, category_id, content, image, claps, shares, num_of_post_views, "
			+ "author, published, date_created, last_updated last_updated from post "
			+ "order by date_created desc "
			+ "limit :limit offset :offset";
	
	private static final String TITLE = "title";
	private static final String POST_ID = "post_id";

	@Override
	public Post findById(long id) {
		return null;

	}

	@Override
	public List<Post> findAll(int size, int page) {
		int offset = (page - 1) * size;
		var mapps = new MapSqlParameterSource();
		mapps.addValue("limit", size);
		mapps.addValue("offset", offset);
		return jdbcTemplate.query(FIND_ALL_POST, mapps, new PostRowMapper());
	}
	
	private static final String ADD_POST = 
			"INSERT INTO POST (title, category_id, content, image, claps, shares, "
			+ "num_of_post_views, author, published, date_created, last_updated) "
			+ "VALUES (:title, :category, :content, :image, 0, 0, 0,  :author, "
			+ "DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp), "
			+ "DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp), "
			+ "DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp))";
	
	@Override
	public long insert(Post post) {
		GeneratedKeyHolder gkh = new GeneratedKeyHolder();
		var mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue(TITLE, post.getTitle());
		mapParameterSource.addValue("category", post.getCategoryId());
		mapParameterSource.addValue("content", post.getContent());
		mapParameterSource.addValue("image", post.getImage());
		mapParameterSource.addValue("author", post.getAuthor());
		jdbcTemplate.update(ADD_POST, mapParameterSource, gkh);
		return Long.valueOf(String.valueOf(gkh.getKeys().get(POST_ID)));
	}

	private static final String UPDATE_POST = ""
			+ "update post set title = :title, image = :image, content := content "
			+ "where post_id = :post_id";
	
	@Override
	public void update(long id, Post entity) {
		var mapps = new MapSqlParameterSource();
		mapps.addValue(TITLE, entity.getTitle());
		mapps.addValue("image", entity.getImage());
		mapps.addValue("content", entity.getContent());
		jdbcTemplate.update(UPDATE_POST, mapps);
	}

	@Override
	public void delete(long id) {
	}

	@Override
	public Optional<Post> existsByTitle(String title) {
		var sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue(TITLE, title);
		var post = jdbcTemplate.queryForObject(FIND_BY_TITLE, sqlParameterSource, new PostRowMapper());
		return Optional.ofNullable(post);
	}
	
	private static final String FIND_BY_ID = "select post_id, title, image, claps, content, "
			+ "author, shares, num_of_post_views, category_id, published, "
			+ "date_created, last_updated from post where post_id = :id";
	
	public Optional<Post> findOptionalPostById(long id) {
		var sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("id", id);
		var user = jdbcTemplate.queryForObject(FIND_BY_ID, sqlParameterSource, new PostRowMapper());
		return Optional.ofNullable(user);
	}

	@Override
	public List<Post> findPostsByCategory(String category) {
		var sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("category", category);
		return jdbcTemplate.query(FIND_BY_CATEGORY, sqlParameterSource, new PostRowMapper());
	}

	private static final String INSERT_CREATED_POST = "insert into created_posts"
			+ "(user_id, post_id) VALUES (:user_id, :post_id)";
	
	@Override
	public void insertCreatedPost(int userId, long postId) {
		var mapps = new MapSqlParameterSource();
		mapps.addValue("user_id", userId);
		mapps.addValue(POST_ID, postId);
		jdbcTemplate.update(INSERT_CREATED_POST, mapps);
	}

	private static final String INSERT_CATEGORY_POST = "insert into category_posts"
			+ "(category_id, post_id) VALUES (:category_id, :post_id)";
	
	@Override
	public void insertCategoryPost(int categoryId, long postId) {
		var mapps = new MapSqlParameterSource();
		mapps.addValue("category_id", categoryId);
		mapps.addValue(POST_ID, postId);
		jdbcTemplate.update(INSERT_CATEGORY_POST, mapps);
	}
}
