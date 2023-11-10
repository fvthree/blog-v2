package com.fvthree.blogpost.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.jdbc.core.RowMapper;

import com.fvthree.blogpost.post.entity.Post;

public class PostRowMapper implements RowMapper<Post> {
	
	@Override
	public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
		var post = new Post();
		post.setId(rs.getLong("post_id"));
		post.setTitle(rs.getString("title"));
		post.setImage(rs.getString("image"));
		post.setClaps(rs.getInt("claps"));
		post.setContent(rs.getString("content"));
		post.setAuthor(rs.getInt("author"));
		post.setShares(rs.getInt("shares"));
		post.setCategoryId(rs.getInt("category_id"));
		post.setNumOfPostViews(rs.getInt("num_of_post_views"));
		DateTimeFormatter formatter = null;
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		post.setPublished(LocalDateTime.parse(rs.getString("published"), formatter));
		post.setDateCreated(LocalDateTime.parse(rs.getString("date_created"), formatter));
		post.setLastUpdated(LocalDateTime.parse(rs.getString("last_updated"), formatter));
		return post;
	}
}
