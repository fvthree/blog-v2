package com.fvthree.blogpost.comment.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.fvthree.blogpost.comment.entity.Comment;
import com.fvthree.blogpost.rowmappers.CommentRowMapper;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public class CommentDaoImpl implements CommentDao {
	
	private static Logger logger = LogManager.getLogger(CommentDaoImpl.class);
	
	private static final String COMMENT_ID = "COMMENT_ID";
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public Comment findById(long id) {
		return null;
	}

	@Override
	public List<Comment> findAll(int page, int size) {
		return null;
	}
	
	private static final String INSERT_COMMENT = "insert into comment(comment, author, date_created) "
			+ "VALUES (:comment, :author, DATE_TRUNC('second', CURRENT_TIMESTAMP::timestamp))";

	@Override
	public long insert(Comment entity) {
		GeneratedKeyHolder gkh = new GeneratedKeyHolder();
		try {
			var mapps = new MapSqlParameterSource();
			mapps.addValue("comment", entity.getBody());
			mapps.addValue("author", entity.getAuthor());
			jdbcTemplate.update(INSERT_COMMENT, mapps, gkh);
		} catch(Exception e) {
			logger.error("error insert = {}", e.getMessage());
		}
		return Long.valueOf(String.valueOf(gkh.getKeys().get(COMMENT_ID)));
	}

	@Override
	public void update(long id, Comment entity) {
		throw new UnsupportedOperationException();
	}

	private static final String DELETE_COMMENT = ""
			+ "delete from comment where COMMENT_ID = :id";
	
	@Override
	public void delete(long id) {
		var mapps = new MapSqlParameterSource();
		mapps.addValue(COMMENT_ID, id);
		jdbcTemplate.update(DELETE_COMMENT, mapps);
	}
	
	private static final String INSERT_POST_COMMENTS = ""
			+ "insert into post_comments(post_id,COMMENT_ID) "
			+ "values (:post_id, :COMMENT_ID)";
	

	@Override
	public void insertPostComments(long postId, long commentId) {
		var mapps = new MapSqlParameterSource();
		mapps.addValue("post_id", postId);
		mapps.addValue(COMMENT_ID, commentId);
		jdbcTemplate.update(INSERT_POST_COMMENTS, mapps);
	}
	
	private static final String GET_COMMENTS_BY_POST_ID = ""
			+ "select c.COMMENT_ID, c.comment, c.author, c.date_created from post_comments pc, comment c "
			+ "where pc.COMMENT_ID = c.COMMENT_ID and pc.post_id = :post_id";

	@Override
	public List<Comment> getAllCommentsByPostId(long postId) {
		var mapps = new MapSqlParameterSource();
		mapps.addValue("post_id", postId);
		return jdbcTemplate.query(GET_COMMENTS_BY_POST_ID, mapps, new CommentRowMapper());
	}
	
	private static final String UPDATE_COMMENT = 
			"update comment set comment = :comment where COMMENT_ID = :COMMENT_ID";
	
	@Override
	public void updateComment(long id, String comment) {
		var mapps = new MapSqlParameterSource();
		mapps.addValue("comment", comment);
		mapps.addValue(COMMENT_ID, id);
		jdbcTemplate.update(UPDATE_COMMENT, mapps);
	}

}
