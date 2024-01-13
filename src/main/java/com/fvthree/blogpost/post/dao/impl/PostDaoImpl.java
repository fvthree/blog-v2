package com.fvthree.blogpost.post.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.fvthree.blogpost.post.entity.Post;
import com.fvthree.blogpost.rowmappers.PostRowMapper;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public class PostDaoImpl implements PostDao {
	
	@Autowired
	private final JdbcClient jdbcClient;
	
	public PostDaoImpl(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}
	
	private static final String TITLE = "title";
	private static final String POST_ID = "post_id";

	@Override
	public Post findById(long id) {
		return null;

	}

	private static final String FIND_ALL_POST = "select "
			+ "post_id, title, category_id, content, image, claps, shares, num_of_post_views, "
			+ "author, published, date_created, last_updated last_updated from post "
			+ "order by date_created desc "
			+ "limit :limit offset :offset";

	@Override
	public List<Post> findAll(int size, int page) {
		int offset = (page - 1) * size;
		return jdbcClient.sql(FIND_ALL_POST).param("limit", size).param("offset", offset).query(new PostRowMapper()).list();
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
		jdbcClient.sql(ADD_POST)
				.param(TITLE, post.getTitle())
				.param("category", post.getCategoryId())
				.param("content", post.getContent())
				.param("image", post.getImage())
				.param("author", post.getAuthor())
				.update(gkh);
		return Long.valueOf(String.valueOf(gkh.getKeys().get(POST_ID)));
	}

	private static final String UPDATE_POST = ""
			+ "update post set title = :title, image = :image, content := content "
			+ "where post_id = :post_id";
	
	@Override
	public void update(long id, Post entity) {
		jdbcClient.sql(UPDATE_POST)
				.param(TITLE, entity.getTitle())
				.param("image", entity.getImage())
				.param("content", entity.getContent())
				.update();
	}

	@Override
	public void delete(long id) {
	}

	private static final String FIND_BY_TITLE = "select post_id, title, category, "
			+ "content_one, content_two, image, author, date_created, last_updated "
			+ "from post where title = :title";

	@Override
	public Optional<Post> existsByTitle(String title) {
		return jdbcClient.sql(FIND_BY_TITLE).param(TITLE, title).query(new PostRowMapper()).optional();
	}
	
	private static final String FIND_BY_ID = "select post_id, title, image, claps, content, "
			+ "author, shares, num_of_post_views, category_id, published, "
			+ "date_created, last_updated from post where post_id = :id";
	
	public Optional<Post> findOptionalPostById(long id) {
		return jdbcClient.sql(FIND_BY_ID).param("id", id).query(new PostRowMapper()).optional();
	}


	private static final String FIND_BY_CATEGORY = "select post_id, title, category, "
			+ "content_one, content_two, image, author, date_created, last_updated "
			+ "from post where category = :category";
	@Override
	public List<Post> findPostsByCategory(String category) {
		return jdbcClient.sql(FIND_BY_CATEGORY).param("category", category).query(new PostRowMapper()).list();
	}

	private static final String INSERT_CREATED_POST = "insert into created_posts"
			+ "(user_id, post_id) VALUES (:user_id, :post_id)";
	
	@Override
	public void insertCreatedPost(int userId, long postId) {
		jdbcClient.sql(INSERT_CREATED_POST).param(POST_ID, postId).param("user_id", userId).update();
	}

	private static final String INSERT_CATEGORY_POST = "insert into category_posts"
			+ "(category_id, post_id) VALUES (:category_id, :post_id)";
	
	@Override
	public void insertCategoryPost(int categoryId, long postId) {
		jdbcClient.sql(INSERT_CATEGORY_POST).param("category_id", categoryId).param(POST_ID, postId).update();
	}
}
